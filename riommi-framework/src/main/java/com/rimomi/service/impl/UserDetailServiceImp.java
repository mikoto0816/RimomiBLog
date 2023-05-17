package com.rimomi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.entity.LoginUser;
import com.rimomi.domain.entity.User;
import com.rimomi.mapper.MenuMapper;
import com.rimomi.mapper.UserMapper;
import com.rimomi.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //根据用户名查询信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(wrapper);
        //判断是否查到用户信息
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在或未登录");
        }
        //返回用户信息
        //查询封装权限信息 验证后台用户的校验
        if (SystemConstants.ADMIN.equals(user.getType())) {
            //如果是后台用户查询权限
            List<String> permsList = menuMapper.getPermsById(user.getId());
            //封装到loginUser中
            return new LoginUser(user, permsList);
        }

        return new LoginUser(user,null);
    }
}
