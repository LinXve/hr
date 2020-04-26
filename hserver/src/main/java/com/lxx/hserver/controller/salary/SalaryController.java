package com.lxx.hserver.controller.salary;

import com.lxx.hserver.entity.Department;
import com.lxx.hserver.entity.Salary;
import com.lxx.hserver.service.DepartmentService;
import com.lxx.hserver.service.EmpService;
import com.lxx.hserver.service.SalaryService;
import com.lxx.hserver.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary/sob")
public class SalaryController {
    @Autowired
    private SalaryService salaryService;
    @Autowired
    private DepartmentService departmentService;

    @RequiresPermissions("salary:add")
    @PostMapping("/salary")
    public Result addSalary(Salary salary) {
        if (salaryService.addSalary(salary) == 1) {
            return Result.success("添加成功!");
        }
        return Result.fail("添加失败");
    }

    @GetMapping("/salary")
    @RequiresPermissions("salary:get")
    public List<Salary> salaries() {
        return salaryService.getAllSalary();
    }

    @GetMapping("/deps")
    @RequiresPermissions("salary:getDep")
    public Result getDeps(){
         return Result.success(departmentService.getAlldeps());
    }

    @PutMapping("/salary")
    @RequiresPermissions("salary:update")
    public Result updateSalary(Salary salary){
        if (salaryService.updateSalary(salary)==1){
            return Result.success("更新成功！");
        }
        return Result.fail("更新失败！");
    }
    @RequiresPermissions("salary:delete")
    @DeleteMapping("/salary/{ids}")
    public Result deleteSalary(@PathVariable String ids){
        if (salaryService.deleteSalary(ids)==1){
            return Result.success("删除成功！");
        }
        return Result.fail("删除失败！！");
    }
}
