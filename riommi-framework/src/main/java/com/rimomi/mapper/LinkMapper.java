package com.rimomi.mapper;
import com.rimomi.domain.entity.Link;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 友链(Link)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-16 11:34:01
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}
