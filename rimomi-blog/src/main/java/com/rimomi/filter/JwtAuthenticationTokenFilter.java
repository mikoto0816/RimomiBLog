package com.rimomi.filter;

import com.alibaba.fastjson.JSON;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.LoginUser;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.utils.JwtUtil;
import com.rimomi.utils.RedisCache;
import com.rimomi.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        //没有token就放行
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims =null;
        try {
             claims = JwtUtil.parseJWT(token);

        } catch (Exception e) {
            //token 非法 则响应给前端
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
        //判断是否获取到loginUser
        if (Objects.isNull(loginUser)) {
            //说明登陆过期或者没登陆响应前端重新登陆
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //存入SecurityContextHolder
        //TODO 封装权限信息查询
        UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //如果代码执行到这里就放行
        filterChain.doFilter(request, response);
    }
}
