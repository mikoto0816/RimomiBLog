package com.rimomi.controller;

import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.RoleDto;
import com.rimomi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/system/role/list")
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status){
        return roleService.listRole(pageNum, pageSize, roleName,status);
    }
    @PutMapping("system/role/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody RoleDto role){
        return roleService.changeRoleStatus(role);
    }
    @GetMapping("/system/role/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
