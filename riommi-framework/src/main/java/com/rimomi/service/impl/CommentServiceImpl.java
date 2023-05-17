package com.rimomi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.Comment;
import com.rimomi.domain.vo.CommentVo;
import com.rimomi.domain.vo.PageVo;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.exception.SystemException;
import com.rimomi.mapper.CommentMapper;
import com.rimomi.mapper.UserMapper;
import com.rimomi.service.CommentService;
import com.rimomi.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-16 15:33:26
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult getCommentList(String type, Long articleId, Integer pageNum, Integer pageSize) {
        //查询所有评论
        //查询根评论
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(type),Comment::getArticleId, articleId);
        commentWrapper.eq(Comment::getRootId, SystemConstants.ROOT_STATUS_NORMAL);
        commentWrapper.eq(Comment::getType,type);
        Page<Comment> commentPage = new Page<>(pageNum, pageSize);
        page(commentPage, commentWrapper);
        //封装成vo
        //因为没有对应的字段所以手动设置
        List<CommentVo> commentVos = toCommentVoList(commentPage.getRecords());

        /**********----------查询所有评论子评论集合并赋值给vo相应的值⬇️-----------***********/
        /**********----------查询所有评论子评论集合并赋值给vo相应的值⬇️-----------***********/
        /**********----------查询所有评论子评论集合并赋值给vo相应的值⬇️-----------***********/
        //TODO 写stream流格式

        for (CommentVo commentVo : commentVos) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }
        //返回结果
        return ResponseResult.okResult(new PageVo(commentVos, commentPage.getTotal()));
    }

    /**
     * 新增评论
     * @param comment
     * @return
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 手动设置vo类型
     *
     * @param records
     * @return
     */
    private List<CommentVo> toCommentVoList(List<Comment> records) {
        //实现bean拷贝
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(records, CommentVo.class);
        //先把得到的list遍历并给userName赋值
        commentVos.stream().
                forEach(commentVo -> commentVo.setUsername(userMapper.selectById(commentVo.getCreateBy())
                .getNickName()));
        //过滤id为-1的根评论并且遍历并给toCommentUsername赋值
            commentVos.stream()
            .filter(commentVo -> (commentVo.getToCommentUserId() !=
            Integer.parseInt(SystemConstants.ROOT_STATUS_NORMAL)))
            .forEach(commentVo -> commentVo
            .setToCommentUserName(userMapper.selectById(commentVo.getToCommentId())
            .getNickName()));

        //返回处理后的结果
        return commentVos;
    }


    private List<CommentVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        List<CommentVo> commentVos = getCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> getCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userMapper.selectById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentUserName = userMapper.selectById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}
