package com.rimomi.service.impl;

import com.rimomi.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * //判断用户是否具有权限
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission){
        if (SecurityUtils.isAdmin()) {
            //如果是超级管理员直接返回true
            return true;
        }
        //否则获取当前登陆用户所具有的权限列表再判断
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permissions);
    }
}
