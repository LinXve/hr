package com.lxx.hserver.controller.salary;

import com.github.pagehelper.PageInfo;
import com.lxx.hserver.entity.Employee;
import com.lxx.hserver.entity.Salary;
import com.lxx.hserver.service.EmpService;
import com.lxx.hserver.service.SalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/salary/sobcfg")
public class SalaryEmpController {
    @Autowired
    private SalaryService salaryService;
    @Autowired
    private EmpService empService;

    @GetMapping("/salaries")
    public List<Salary> salaries() {
        return salaryService.getAllSalary();
    }

    @GetMapping("/emp")
    public Map<String, Object> getEmployeeByPage(@RequestParam(defaultValue = "1",value = "page") Integer page,@RequestParam(defaultValue = "",value = "keywords")String keywords) {
        Map<String,Object> map=new HashMap<>();
        List<Employee> emps = empService.getEmpAndSalaryByPage(page,keywords);
        int count = empService.getCountByEmpDepSal(keywords);
        map.put("emps",emps);
        map.put("count",count);
        return map;
    }
}
