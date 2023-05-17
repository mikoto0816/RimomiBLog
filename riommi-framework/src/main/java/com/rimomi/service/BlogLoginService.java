package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author rimomi
 * @since 2022-09-16 12:35:51
 */
public interface BlogLoginService{

    ResponseResult login(User user);

    ResponseResult logout();
}
