package com.rimomi.mapper;
import com.rimomi.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-19 09:49:48
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<String> getRoleKeyList(Long id);
}
