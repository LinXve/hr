package com.lxx.hserver.service;

import com.lxx.hserver.entity.Department;
import com.lxx.hserver.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;
    public List<Department> getDepByPid(Integer id) {
        return departmentMapper.getDepByPid(id);
    }

    public List<Department> getAlldeps() {
        return departmentMapper.getDepByPid(-1);
    }

    public int addDep(Department department) {
        return departmentMapper.addDep(department);
    }

    public int deleteDepById(Integer id) {
        return departmentMapper.deleteDep(id);
    }
}
