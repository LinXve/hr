package com.lxx.hserver.service;

import com.lxx.hserver.entity.Employee;
import com.lxx.hserver.entity.Nation;
import com.lxx.hserver.entity.PoliticsStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class EmpServiceTest {
    @Autowired
    EmpService empService;
    @Test
    void getAllNations() {
        List<Nation> allNations = empService.getAllNations();
        for (Nation nation : allNations) {
            System.out.println(nation.toString());
        }
    }
    @Test
    void getAllPolitics(){
        List<PoliticsStatus> politics = empService.getAllPolitics();
        for (PoliticsStatus politic : politics) {
            System.out.println(politic.toString());
        }
    }
    @Test
    void getMaxWorkID(){
        Long maxWorkID = empService.getMaxWorkID();
        System.out.println(maxWorkID);
    }
    @Test
    void export(){
        List<Employee> allEmployees = empService.getAllEmployees();
        for (Employee allEmployee : allEmployees) {
            System.out.println(allEmployee.getName());
        }
    }
}