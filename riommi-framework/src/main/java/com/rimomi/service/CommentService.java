package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author rimomi
 * @since 2022-09-16 15:33:26
 */
public interface CommentService extends IService<Comment> {

    ResponseResult getCommentList(String type, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
