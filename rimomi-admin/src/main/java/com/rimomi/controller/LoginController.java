package com.rimomi.controller;

import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.LoginUser;
import com.rimomi.domain.entity.Menu;
import com.rimomi.domain.entity.User;
import com.rimomi.domain.vo.AdminUserInfoVo;
import com.rimomi.domain.vo.RoutersVo;
import com.rimomi.domain.vo.UserInfoVo;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.exception.SystemException;
import com.rimomi.service.BlogLoginService;
import com.rimomi.service.LoginService;
import com.rimomi.service.MenuService;
import com.rimomi.service.RoleService;
import com.rimomi.utils.BeanCopyUtils;
import com.rimomi.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService LoginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return LoginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return LoginService.logout();
    }
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登陆的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据id查询权限信息
        List<String> menuLis=menuService.getpermisById(loginUser.getUser().getId());
        //根据id查询角色信息
        List<String> roleKeyList= roleService.getRoleKeyList(loginUser.getUser().getId());
        //获取用户信息
        User user = loginUser.getUser();
        //封装数据返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(menuLis, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> routers(){
        Long userId = SecurityUtils.getUserId();
        //查询menu结果是tree的形式（体现子父级关系）
        List<Menu> menu = menuService.routers(userId);
        //封装vo返回
        return ResponseResult.okResult(new RoutersVo(menu));
    }
}
