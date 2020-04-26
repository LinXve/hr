package com.lxx.hserver.service;

import com.lxx.hserver.entity.Salary;
import com.lxx.hserver.mapper.SalaryMapper;
import com.lxx.hserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalaryService {
    @Autowired
    private SalaryMapper salaryMapper;

    @Transactional
    public int addSalary(Salary salary) {
        int i = salaryMapper.addSalary(salary);
        if (i == 1) {
            if (salaryMapper.addDepartmentSalary(salary.getId(), salary.getDepartmentId()) == 1) {
                return i;
            }
        }
        throw new RuntimeException("插入失败！");
    }

    public List<Salary> getAllSalary() {
        return salaryMapper.getAllSalary();
    }

    @Transactional
    public int updateSalary(Salary salary) {
        if (salaryMapper.updateDepartmentSalary(salary.getId(), salary.getDepartmentId()) == 1) {
            return salaryMapper.updateSalary(salary);
        }
        throw new RuntimeException("更新失败！");
    }

    public int deleteSalary(String ids) {
        String[] split = ids.split(",");
        return salaryMapper.deleteSalary(split);
    }
}
