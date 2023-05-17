package com.rimomi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.User;
import com.rimomi.domain.entity.UserRole;
import com.rimomi.domain.vo.AddUserVo;
import com.rimomi.domain.vo.PageVo;
import com.rimomi.domain.vo.UserInfoVo;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.exception.SystemException;
import com.rimomi.mapper.UserMapper;
import com.rimomi.service.UserRoleService;
import com.rimomi.service.UserService;
import com.rimomi.utils.BeanCopyUtils;
import com.rimomi.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.awt.peer.ScrollbarPeer;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-17 08:26:38
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;
    /**
     * 个人信息
     * @return
     */
    @Override
    public ResponseResult userInfo() {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //根据id查询
        User user = getById(userId);
        //封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    /**
     * 个人信息更新
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(User user) {

        updateById(user);

        return ResponseResult.okResult();

    }


    //获取passwordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public ResponseResult register(User user) {
        //TODO 使用校验框架
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }



    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickName,nickName);
        return count(wrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        return count(wrapper) > 0;
    }
    private boolean phoneNumberExist(String phoneNumber) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhonenumber,phoneNumber);
        return count(wrapper) > 0;
    }
    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail,email);
        return count(wrapper) > 0;
    }



    @Override
    public ResponseResult userList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        wrapper.like(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        wrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> userPage = new Page<>(pageNum, pageSize);
        page(userPage, wrapper);
        PageVo pageVo = new PageVo(userPage.getRecords(), userPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    /**
     * 新增用户
     * @param user
     * @return
     */
    @Override
    @Transactional
    public ResponseResult addUser(AddUserVo user) {

        //TODO 使用校验框架
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(phoneNumberExist(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if(emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        User copyUser = BeanCopyUtils.copyBean(user, User.class);
        save(copyUser);
        //新增用户可以直接关联角色
        List<Long> roleIds = user.getRoleIds();
        roleIds.stream().forEach(roleId -> userRoleService.save(new UserRole(copyUser.getId(), roleId)));
        return ResponseResult.okResult("添加成功");
    }
}
