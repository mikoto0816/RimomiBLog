package com.rimomi.runner;

import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.entity.Article;
import com.rimomi.mapper.ArticleMapper;
import com.rimomi.service.ArticleService;
import com.rimomi.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@Async
public class ViewCountRunner implements CommandLineRunner{

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleMapper articleService;

    /**
     * 实现CommandLineRunner接口后重写run方法就可以做定时任务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        //查询博客viewCount信息
        List<Article> list = articleService.selectList(null);
        Map<String, Integer> viewCountMap = list.stream().collect(Collectors.toMap(article -> article.getId().toString(),
                article -> article.getViewCount().intValue()));
        //存储到redis中，以article:viewCount为key value为id和viewCount的map存储
        redisCache.setCacheMap(SystemConstants.VIEWCOUNT,viewCountMap);
    }
}
