package com.lxx.hserver.mapper;

import com.hazelcast.security.permission.ListPermission;
import com.lxx.hserver.entity.Permission;
import com.lxx.hserver.entity.PermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PermissionMapper {
    List<Permission> getAll(@Param("rid") Integer rid);
}