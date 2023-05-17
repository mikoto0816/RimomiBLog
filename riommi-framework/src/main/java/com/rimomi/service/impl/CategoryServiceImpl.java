package com.rimomi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.Article;
import com.rimomi.domain.entity.Category;
import com.rimomi.domain.vo.CategoryVo;
import com.rimomi.service.CategoryService;
import com.rimomi.service.ArticleService;
import com.rimomi.mapper.CategoryMapper;
import com.rimomi.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-16 10:26:51
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //	注意： ①要求只展示有发布正式文章的分类 ②必须是正常状态的分类
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = (List<Article>) articleService.list(articleWrapper);
        //获取正式发布文章的id并且去重
        Set<Long> IdConllect = list.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //根据这些id去查询分类
        List<Category> categoryList = listByIds(IdConllect);
        //找出正常分类
        categoryList.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //页面上需要展示分类列表，用户可以点击具体的分类查看该分类下的文章列表。
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);


    }
}
