package com.rimomi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.AddArticleDto;
import com.rimomi.domain.entity.AdminArticle;
import com.rimomi.domain.entity.Article;
import com.rimomi.domain.entity.ArticleTag;
import com.rimomi.domain.vo.ArticleDetailVo;
import com.rimomi.domain.vo.ArticleListVo;
import com.rimomi.domain.vo.HotArticleVo;
import com.rimomi.domain.vo.PageVo;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.service.ArticleTagService;
import com.rimomi.service.CategoryService;
import com.rimomi.utils.BeanCopyUtils;
import com.rimomi.mapper.ArticleMapper;
import com.rimomi.utils.RedisCache;
import com.rimomi.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rimomi.service.ArticleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-16 09:02:36
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult getHotArticleList() {
        //	注意：不能把草稿展示出来，不能把删除了的文章查询出来。要按照浏览量进行降序排序。
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getViewCount);
        //需要查询浏览量最高的前10篇文章的信息。要求展示文章标题和浏览量。
        Page<Article> articlePage = new Page<>(1,10);
        Page<Article> page = page(articlePage, wrapper);
        //要求展示文章标题和浏览量。
        List<Article> records = page.getRecords();
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        // 把能让用户自己点击跳转到具体的文章详情进行浏览。
        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //	首页：查询所有的文章
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        //	分类页面：查询对应分类下的文章
        pageSize=10;
        wrapper.eq(!Objects.isNull(categoryId)&&categoryId>0,
                Article::getCategoryId, categoryId);
        //	要求：①只能查询正式发布的文章 ②置顶的文章要显示在最前面
        wrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        wrapper.orderByDesc(Article::getIsTop);
        Page<Article> page = new Page<>(pageNum,pageSize);
        Page<Article> articlePage = page(page, wrapper);
        List<Article> records = articlePage.getRecords();
        records.stream()
                .filter(article -> StringUtils.hasText(categoryService.getById(article.getCategoryId()).getName()))
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
//        ArrayList<Article> list = new ArrayList<>();
//        Page<Article> articlePage1 = new Page<>();
//        for (Article record : records) {
//            String name = categoryService.getById(record.getCategoryId()).getName();
//            record.setCategoryName(name);
//            list.add(record);
//            articlePage1.setRecords(list);
//        }
//        List<Article> records1 = articlePage1.getRecords();

//        首先封装articleListVo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(records, ArticleListVo.class);
        //把artclelistvo赋值给pageVo
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);


    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //	要求：在文章详情中展示其分类名
        Article articleServiceById = articleService.getById(id);
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.VIEWCOUNT, id.toString());
        //如果不为空则更新浏览次数
        if (Objects.nonNull(viewCount)) {
            articleServiceById.setViewCount(viewCount.longValue());
        }
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(articleServiceById, ArticleDetailVo.class);
        //	要求：在文章详情中展示其分类名
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        //设置分类名
        articleDetailVo.setCategoryName(categoryService.getById(categoryId).getName());
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //redis的key为SystemConstants.VIEWCOUNT
        //更新redis中id为key的浏览量的数据
        redisCache.incrementCacheMapValue(SystemConstants.VIEWCOUNT,id.toString(),1);
        return ResponseResult.okResult();
    }

    /**
     * 后台添加文章 //TODO 写完图片上传再检查
     * @param articleDto
     * @return
     */
    @Override
    @Transactional  //开启事务
    public ResponseResult addArticle(AddArticleDto articleDto) {
        //添加文章
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        article.setCreateBy(SecurityUtils.getUserId());
        //Article article = BeanCopyUtils.copyBean(adminArticle, Article.class);
        save(article);
        //添加tag关系
        List<Long> tags = articleDto.getTags();
        List<ArticleTag> articleTags = tags.stream().map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 后台文章查询
     * @param pageNum
     * @param pageSize
     * @param title
     * @param summary
     * @return
     */
    @Override
    public ResponseResult adminArticleList(Integer pageNum, Integer pageSize,String title, String summary) {
        //查询全部未被删除文章包括草稿
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        wrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article> articlePage = new Page<>(pageNum, pageSize);
        page(articlePage, wrapper);
        PageVo pageVo = new PageVo(articlePage.getRecords(), articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 后台回显文章
     * @param id
     * @return
     */
    @Override
    public ResponseResult reverseArticle(Long id) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ArticleTag> articleTagWrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getId, id);
        Article article = getOne(wrapper);
        //查询id对应的tag
        articleTagWrapper.eq(ArticleTag::getTagId,id);
        List<ArticleTag> tagList = articleTagService.list(articleTagWrapper);
        List<Long> collect = tagList.stream().map(o -> o.getTagId()).collect(Collectors.toList());
        //封装给dto返回
        AddArticleDto addArticleDto = BeanCopyUtils.copyBean(article, AddArticleDto.class);
        addArticleDto.setTags(collect);
        return ResponseResult.okResult(addArticleDto);
    }

    /**
     * 后台更新文章
     * @param updateArticle
     * @return
     */
    @Override
    public ResponseResult AdminUpdateArticle(AddArticleDto updateArticle) {
        Article article = BeanCopyUtils.copyBean(updateArticle, Article.class);
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, updateArticle.getId());
        //更新文章
        update(article,wrapper);
        //更新tag
        List<Long> tags = updateArticle.getTags();
        List<ArticleTag> collect = tags.stream().map(tag -> new ArticleTag(updateArticle.getCategoryId(), tag))
                .collect(Collectors.toList());
        articleTagService.updateBatchById(collect);
        return ResponseResult.okResult("修改成功");
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @Override
    public ResponseResult AdminDeleteArticle(Long id) {
        //根据id删除
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id!=null,Article::getId, id);
        remove(wrapper);
        return ResponseResult.okResult("删除成功");
    }

}
