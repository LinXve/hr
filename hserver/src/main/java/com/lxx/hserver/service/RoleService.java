package com.lxx.hserver.service;

import com.lxx.hserver.entity.Role;
import com.lxx.hserver.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    public List<Role> allRoles() {
        return roleMapper.getAllRoles();
    }

    public int deleteRoleById(Integer rid) {
        return roleMapper.deleteByPrimaryKey(rid);
    }

    public int addRole(String role, String description) {
        Role role1 = new Role();
        role1.setRole(role);
        role1.setDescription(description);
        role1.setAvailable(true);
        return roleMapper.insertSelective(role1);
    }

    public int updateRolePermission(Integer rid, String[] pid) {
        if (pid.length==0){
            return 0;
        }
       return roleMapper.addPermissionWithRole(rid,pid);
    }

    public int deleteRolePermission(Integer rid, Integer pid) {
        if (!StringUtils.isEmpty(rid)&&!StringUtils.isEmpty(pid)){
            return roleMapper.deleteRolePermission(rid,pid);
        }
        return 0;
    }

    public int addRolePermission(Integer rid, String[] pids) {
        return roleMapper.addPermissionWithRole(rid,pids);
    }
}
