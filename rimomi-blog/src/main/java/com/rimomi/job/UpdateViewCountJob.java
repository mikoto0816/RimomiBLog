package com.rimomi.job;

import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.entity.Article;
import com.rimomi.mapper.ArticleMapper;
import com.rimomi.service.ArticleService;
import com.rimomi.utils.RedisCache;
import com.rimomi.utils.TaskUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    //todo Scheduled注入级别高于autowired
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;
   // private static ApplicationContext context;
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        context=applicationContext;
//    }
//    public static ApplicationContext getApplicationContext() {
//        return context;
//    }
//    public static Object getBean(String name){
//        return getApplicationContext().getBean(name);
//    }

   @Scheduled(cron = "0/10 * * * * ?")
    public void updateViewCount(){
        //获取redis的浏览量
       //RedisCache redisCache = (RedisCache) getBean("redisCache");
       //ArticleService articleService = (ArticleService) getBean("articleService");
       Map<String, Integer> redisViewCountMap = redisCache.getCacheMap(SystemConstants.VIEWCOUNT);
        List<Article> collect = redisViewCountMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库
        articleService.updateBatchById(collect);
    }



}
