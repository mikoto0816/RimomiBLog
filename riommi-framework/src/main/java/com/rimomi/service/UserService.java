package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.User;
import com.rimomi.domain.vo.AddUserVo;


/**
 * 用户表(User)表服务接口
 *
 * @author rimomi
 * @since 2022-09-17 08:26:38
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(AddUserVo user);
}
