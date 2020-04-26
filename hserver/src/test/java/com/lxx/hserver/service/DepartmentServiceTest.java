package com.lxx.hserver.service;

import com.lxx.hserver.entity.Department;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class DepartmentServiceTest {
    @Autowired
    DepartmentService departmentService;
    @Test
    void getAllDepartments() {
        List<Department> departments = departmentService.getDepByPid(-1);
        for (Department department : departments) {
            System.out.println(department.toString());
        }
    }
    @Test
    void addDep(){
        Department department = new Department();
        department.setParentId(-1);
        department.setName("食试试");
        departmentService.addDep(department);
        System.out.println(department.toString());
    }
}