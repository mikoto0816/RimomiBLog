package com.rimomi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.AddArticleDto;
import com.rimomi.domain.entity.Article;
import com.rimomi.domain.entity.ArticleTag;
import com.rimomi.service.ArticleTagService;
import com.rimomi.mapper.ArticleTagMapper;
import com.rimomi.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-19 15:25:30
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {


}
