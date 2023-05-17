package com.rimomi.mapper;
import com.rimomi.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-16 15:33:26
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
