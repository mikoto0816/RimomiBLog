package com.rimomi.controller;

import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.Comment;
import com.rimomi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult getCommentList(String type,Long articleId,Integer pageNum,Integer pageSize){
        return commentService.getCommentList(SystemConstants.ARTICLE_COMMENT, articleId,pageNum,pageSize);
    }
    @GetMapping("/linkCommentList")
    public ResponseResult getLinkCommentList(String type,Long articleId,Integer pageNum,Integer pageSize){
        return commentService.getCommentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}
