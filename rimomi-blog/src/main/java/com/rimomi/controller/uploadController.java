package com.rimomi.controller;

import com.rimomi.domain.ResponseResult;
import com.rimomi.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class uploadController {

    @Autowired
    private UploadService uploadService;
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile img){
        return uploadService.upload(img);
    }
}
