package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author rimomi
 * @since 2022-09-16 10:26:51
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

}
