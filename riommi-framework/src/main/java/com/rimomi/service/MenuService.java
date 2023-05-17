package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author rimomi
 * @since 2022-09-19 09:48:26
 */
public interface MenuService extends IService<Menu> {

    List<String> getpermisById(Long id);

    List<Menu>routers(Long userId);

    ResponseResult getMenu(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult findMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenuById(Long id);
}
