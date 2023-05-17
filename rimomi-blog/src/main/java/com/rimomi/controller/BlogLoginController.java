package com.rimomi.controller;

import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.User;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.exception.SystemException;
import com.rimomi.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
//        if (StringUtils.hasText(user.getUserName())) {
//            //必须传用户名
//            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
//        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
