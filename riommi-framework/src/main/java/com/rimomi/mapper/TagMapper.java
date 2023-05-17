package com.rimomi.mapper;
import com.rimomi.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 标签(Tag)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-19 13:53:30
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
