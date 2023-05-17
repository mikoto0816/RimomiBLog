package com.rimomi.controller;

import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.RoleDto;
import com.rimomi.domain.entity.Menu;
import com.rimomi.domain.entity.Role;
import com.rimomi.service.MenuService;
import com.rimomi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminMenuController {

    @Autowired
    private MenuService menuService;



    @GetMapping("/system/menu/list")
    public ResponseResult getMenu(String status, String menuName){
        return menuService.getMenu(status, menuName);
    }
    @PostMapping("/system/menu")
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }
    @GetMapping("/system/menu/{id}")
    public ResponseResult findMenuById(@PathVariable Long id){
        return menuService.findMenuById(id);
    }
    @PutMapping("/system/menu")
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }
    @DeleteMapping("/system/menu/{id}")
    public ResponseResult deleteMenuById(@PathVariable Long id){
        return menuService.deleteMenuById(id);
    }

}
