package com.rimomi.mapper;
import com.rimomi.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-19 15:25:30
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}
