package com.rimomi.mapper;
import com.rimomi.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 文章表(Article)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-16 09:02:34
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
