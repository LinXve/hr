package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.Role;
import com.lxx.hserver.entity.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    long countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer rid);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer rid);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> selectRolesByUid(@Param("uid") Integer uid);

//    查询某个角色下的权限
    Role selectRolePermissionByRid(@Param("rid") Integer rid);

    List<Role> getAllRoles();

    int deleteRolePermission(@Param("rid") Integer rid,@Param("pid") Integer pid);

    int addPermissionWithRole(@Param("rid") Integer rid,@Param("pids") String[] pids);
}