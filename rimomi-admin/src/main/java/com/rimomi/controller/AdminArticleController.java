package com.rimomi.controller;

import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.AddArticleDto;
import com.rimomi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class AdminArticleController {

    @Autowired
    private ArticleService ArticleService;

    @PostMapping("/article")
    public ResponseResult addArticle(@RequestBody AddArticleDto articleDto){
        return ArticleService.addArticle(articleDto);
    }
    @GetMapping("/article/list")
    public ResponseResult adminArticleList(Integer pageNum, Integer pageSize,String title,String summary){
        return ArticleService.adminArticleList(pageNum, pageSize,title, summary);
    }
    @GetMapping("/article/{id}")
    public ResponseResult reverseArticle(@PathVariable Long id){
        return ArticleService.reverseArticle(id);
    }
    @PutMapping("/article")
    public ResponseResult updateArticle(@RequestBody AddArticleDto updateArticle){
        return ArticleService.AdminUpdateArticle(updateArticle);
    }
    @DeleteMapping("/article/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        return ArticleService.AdminDeleteArticle(id);
    }
}
