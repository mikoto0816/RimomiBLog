package com.rimomi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.LoginUser;
import com.rimomi.domain.entity.User;
import com.rimomi.domain.vo.BlogUserInfoVo;
import com.rimomi.domain.vo.UserInfoVo;
import com.rimomi.mapper.UserMapper;
import com.rimomi.utils.BeanCopyUtils;
import com.rimomi.utils.JwtUtil;
import com.rimomi.utils.RedisCache;
import com.rimomi.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.rimomi.service.BlogLoginService;

import java.util.Objects;

/**
 * 用户表(SysUser)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-16 12:35:51
 */
@Service("sysUserService")
public class BlogLoginServiceImpl implements BlogLoginService {

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
        redisCache.setCacheObject("bloglogin:"+userId, loginUser);
        //按照前端格式封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserInfoVo blogUserInfoVo = new BlogUserInfoVo(token, userInfoVo);
        return ResponseResult.okResult(blogUserInfoVo);
    }

    @Override
    public ResponseResult logout() {
        //获取userid
        Long userId = SecurityUtils.getUserId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
