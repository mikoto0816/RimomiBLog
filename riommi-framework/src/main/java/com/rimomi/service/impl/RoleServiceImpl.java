package com.rimomi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.RoleDto;
import com.rimomi.domain.entity.Menu;
import com.rimomi.domain.entity.Role;
import com.rimomi.domain.vo.PageVo;
import com.rimomi.service.RoleService;
import com.rimomi.mapper.RoleMapper;
import com.rimomi.utils.BeanCopyUtils;
import com.rimomi.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-19 09:49:48
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> getRoleKeyList(Long id) {
        //如果是管理员返回的集合中只需要有admin
        if (SecurityUtils.isAdmin()) {
            ArrayList<String> list = new ArrayList<>();
            list.add("admin");
            return list;
        }
        //否则返回自己具有的权限
        RoleMapper roleMapper = getBaseMapper();
        return roleMapper.getRoleKeyList(id);
    }

    /**
     * 角色列表
     * @param pageNum
     * @param pageSize
     * @param roleName
     * @param status
     * @return
     */
    @Override
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        wrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        wrapper.orderByAsc(Role::getRoleSort);
        Page<Role> rolePage = new Page<>(pageNum, pageSize);
        page(rolePage, wrapper);
        PageVo pageVo = new PageVo(rolePage.getRecords(), rolePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeRoleStatus(RoleDto role) {
        //通过id查询改变status
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getId,role.getRoleId());
        Role copyBean = BeanCopyUtils.copyBean(role, Role.class);
        copyBean.setId(role.getRoleId());
        update(copyBean, wrapper);
        return ResponseResult.okResult("操作成功");
    }

    @Override
    public ResponseResult listAllRole() {
        //所有状态正常的角色
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Role> list = list(wrapper);
        return ResponseResult.okResult(list);
    }
}
