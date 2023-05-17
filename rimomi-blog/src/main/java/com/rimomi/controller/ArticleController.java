package com.rimomi.controller;



import com.rimomi.domain.ResponseResult;
import com.rimomi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult getHotArticleList(){
        return articleService.getHotArticleList();
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageNum") Integer pageSize,@RequestParam("categoryId")  Long categoryId){
        return articleService.getArticleList(pageNum, pageSize, categoryId);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable Long id){
        return articleService.getArticleDetail(id);
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
