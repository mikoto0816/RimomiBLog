package com.rimomi.mapper;
import com.rimomi.domain.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-25 16:56:45
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
