package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author rimomi
 * @since 2022-09-16 11:34:03
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

}
