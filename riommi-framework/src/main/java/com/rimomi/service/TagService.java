package com.rimomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.TageListDto;
import com.rimomi.domain.entity.Tag;
import com.rimomi.domain.vo.PageVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author rimomi
 * @since 2022-09-19 13:53:30
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> getlist(Integer page, Integer pageSize, TageListDto tageListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult putTag(Tag tag);

    ResponseResult listAllCategory();

    ResponseResult listAllTag();
}
