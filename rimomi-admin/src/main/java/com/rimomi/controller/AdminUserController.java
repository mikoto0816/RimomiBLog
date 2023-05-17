package com.rimomi.controller;

import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.User;
import com.rimomi.domain.vo.AddUserVo;
import com.rimomi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminUserController {

    @Autowired
    private UserService userService;
    @GetMapping("/system/user/list")
    public ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        return userService.userList(pageNum, pageSize, userName, phonenumber, status);
    }
    @PostMapping("/system/user")
    public ResponseResult addUser(@RequestBody AddUserVo user) {
        return userService.addUser(user);
    }
}
