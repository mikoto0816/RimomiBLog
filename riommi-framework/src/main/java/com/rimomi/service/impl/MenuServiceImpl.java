package com.rimomi.service.impl;

import ch.qos.logback.core.pattern.util.AlmostAsIsEscapeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.constants.SystemConstants;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.Menu;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.service.MenuService;
import com.rimomi.mapper.MenuMapper;
import com.rimomi.utils.BeanCopyUtils;
import com.rimomi.utils.SecurityUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-19 09:48:26
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> getpermisById(Long id) {
        //如果是管理员返回所有未被删除的，状态正常的，type为C或F的权限信息
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.BUTTEN, SystemConstants.MENU);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> list = list(wrapper);
            List<String> perms = list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        return getBaseMapper().getPermsById(id);
    }

    /**
     * 动态路由接口
     * @param userId
     */
    @Override
    public List<Menu> routers(Long userId) {
        //如果是管理员获取所有符合要求的menu权限
        List<Menu> menu = null;
        if (SecurityUtils.isAdmin()) {
            menu = getBaseMapper().selectAllRouter();
        }else {
            //否则获取当前用户所具有的menu权限
            menu = getBaseMapper().selectRouterById(userId);
        }
        //tree封装
        //先找出第一层的菜单再去找他们的子菜单
        List<Menu> tree = builderMenuTree(menu,0L);
        return tree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(m -> m.getParentId().equals(parentId))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    /**
     * 菜单分类
     * @param status
     * @param menuName
     * @return
     */
    @Override
    public ResponseResult getMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        wrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        wrapper.orderByAsc(Menu::getParentId).orderByDesc(Menu::getOrderNum);
        List<Menu> list = list(wrapper);
        return ResponseResult.okResult(list);
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @Override
    public ResponseResult addMenu(Menu menu) {
        if (Objects.nonNull(menu)) {
            save(menu);
            return ResponseResult.okResult("操作成功");
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"出现错误");
    }

    /**
     * 根据id查询菜单
     * @param id
     * @return
     */
    @Override
    public ResponseResult findMenuById(Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(id!=null,Menu::getId,id);
        Menu menu = getOne(wrapper);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getId,menu.getId());
        update(menu,wrapper);
        return ResponseResult.okResult("操作成功");
    }

    @Override
    public ResponseResult deleteMenuById(Long id) {
        //根据id删除
        //如果有子菜单则不允许删除
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,id);
        int count = count(wrapper);
        if (count>0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"有子菜单不能删除");
        }
        removeById(id);
        return ResponseResult.okResult("删除成功");
    }


}
