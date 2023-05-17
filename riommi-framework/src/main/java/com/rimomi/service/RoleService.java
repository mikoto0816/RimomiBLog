package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.RoleDto;
import com.rimomi.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author rimomi
 * @since 2022-09-19 09:49:48
 */
public interface RoleService extends IService<Role> {

    List<String> getRoleKeyList(Long id);

    ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeRoleStatus(RoleDto role);

    ResponseResult listAllRole();
}
