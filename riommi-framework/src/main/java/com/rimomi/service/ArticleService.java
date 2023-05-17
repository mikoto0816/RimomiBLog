package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.AddArticleDto;
import com.rimomi.domain.entity.Article;


/**
 * 文章表(Article)表服务接口
 *
 * @author rimomi
 * @since 2022-09-16 09:02:36
 */
public interface ArticleService extends IService<Article> {

    ResponseResult getHotArticleList();

    ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(AddArticleDto articleDto);

    ResponseResult adminArticleList(Integer pageNum, Integer pageSize,String title,String summary);

    ResponseResult reverseArticle(Long id);

    ResponseResult AdminUpdateArticle(AddArticleDto updateArticle);

    ResponseResult AdminDeleteArticle(Long id);
}
