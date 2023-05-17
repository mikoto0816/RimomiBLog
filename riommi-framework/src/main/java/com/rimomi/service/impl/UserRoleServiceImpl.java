package com.rimomi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.domain.entity.UserRole;
import com.rimomi.service.UserRoleService;
import com.rimomi.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-25 16:56:45
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
