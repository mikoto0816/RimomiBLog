package com.rimomi.mapper;
import com.rimomi.domain.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author rimomi
 * @since 2022-09-19 09:48:26
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> getPermsById(Long id);

    List<Menu> selectAllRouter();

    List<Menu> selectRouterById(Long userId);
}
