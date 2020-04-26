package com.lxx.hserver.service;

import com.lxx.hserver.entity.Permission;
import com.lxx.hserver.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> getAllPermission(Integer rid) {
        return permissionMapper.getAll(rid);
    }
}
