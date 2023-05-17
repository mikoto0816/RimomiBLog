package com.rimomi.service.impl;

import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.LoginUser;
import com.rimomi.domain.entity.User;
import com.rimomi.service.LoginService;
import com.rimomi.utils.JwtUtil;
import com.rimomi.utils.RedisCache;
import com.rimomi.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * 用户表(SysUser)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-16 12:35:51
 */
@Service
public class SysLoginServiceImpl implements LoginService {

    //注入AuthenticationManager
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登陆方法
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        //登陆方法实现
        UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否通过认证
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //认证成功利用JWT生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("login:"+userId, loginUser);
        //返回token
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseResult.okResult(map);

    }

    @Override
    public ResponseResult logout() {
        //获取userid
        Long userId = SecurityUtils.getUserId();
        //删除redis中的用户信息
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}
