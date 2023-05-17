package com.rimomi.mapper;
import com.rimomi.domain.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 分类表(Category)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-16 10:26:47
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
