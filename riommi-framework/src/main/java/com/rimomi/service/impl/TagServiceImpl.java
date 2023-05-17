package com.rimomi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rimomi.domain.ResponseResult;
import com.rimomi.domain.dto.TageListDto;
import com.rimomi.domain.entity.Category;
import com.rimomi.domain.entity.Tag;
import com.rimomi.domain.vo.CategoryVo;
import com.rimomi.domain.vo.PageVo;
import com.rimomi.domain.vo.TagVo;
import com.rimomi.enums.AppHttpCodeEnum;
import com.rimomi.mapper.TagMapper;
import com.rimomi.service.CategoryService;
import com.rimomi.service.TagService;
import com.rimomi.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 标签(Tag)表服务实现类
 *
 * @author rimomi
 * @since 2022-09-19 13:53:30
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult<PageVo> getlist(Integer page, Integer pageSize, TageListDto tageListDto) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(tageListDto.getName()),Tag::getName,tageListDto.getName());
        wrapper.eq(StringUtils.hasText(tageListDto.getRemark()),Tag::getRemark,tageListDto.getRemark());
        //分页查询
        Page<Tag> tagPage = new Page<>(page, pageSize);
        page(tagPage,wrapper);
        //封装pageVo返回
        PageVo pageVo = new PageVo(tagPage.getRecords(), tagPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 新增标签
     * @param tag
     * @return
     */
    @Override
    public ResponseResult addTag(Tag tag) {
        String name = tag.getName();
        String remark = tag.getRemark();
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getName, name);
        Tag oneTag = getOne(wrapper);
        if (StringUtils.hasText(name)&&StringUtils.hasText(remark)&&Objects.isNull(oneTag)) {
            //如果不为空则添加tag
            save(tag);
            return ResponseResult.okResult("添加成功");
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"tag已经存在或tag名为空");
    }

    /**
     * 删除tag
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteTag(Long id) {
        if (id != null&&id >0) {
            removeById(id);
            return ResponseResult.okResult("删除成功");
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"tag删除失败");
    }

    /**
     * 回显tag
     * @param id
     * @return
     */
    @Override
    public ResponseResult getTag(Long id) {
        //根据id查询tag回显
        Tag tag = getById(id);
        return ResponseResult.okResult(tag);
    }

    /**
     * 修改标签
     * @param tag
     */
    @Override
    public ResponseResult putTag(Tag tag) {
        if (!Objects.isNull(tag)) {
            updateById(tag);
            return ResponseResult.okResult("修改成功");
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"修改失败");
    }

    /**
     * 查询所有分类
     * @return
     */
    @Override
    public ResponseResult listAllCategory() {
        List<Category> list = categoryService.list();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 查询所有标签
     * @return
     */
    @Override
    public ResponseResult listAllTag() {
        List<Tag> list = list();
        List<TagVo> TagVo = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(TagVo);
    }
}
