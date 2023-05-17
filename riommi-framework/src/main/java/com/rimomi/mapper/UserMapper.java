package com.rimomi.mapper;
import com.rimomi.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-16 13:26:43
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
