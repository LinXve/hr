package com.lxx.hserver.controller.emp;

import com.lxx.hserver.entity.Employee;
import com.lxx.hserver.entity.Position;
import com.lxx.hserver.service.DepartmentService;
import com.lxx.hserver.service.EmpService;
import com.lxx.hserver.service.JobLevelService;
import com.lxx.hserver.service.PositionService;
import com.lxx.hserver.utils.EmailRunnable;
import com.lxx.hserver.utils.ExcelUtils;
import com.lxx.hserver.utils.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/employee/basic")
public class EmpBasicController {
    @Autowired
    EmpService empService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    JobLevelService jobLevelService;
    @Autowired
    PositionService positionService;
    @Autowired
    ExecutorService executorService;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    JavaMailSender javaMailSender;

    //    初始化
    @GetMapping("/basicdata")
    public Map<String, Object> initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("nations", empService.getAllNations());//所有的民族
        map.put("politics", empService.getAllPolitics());//政治面貌

        map.put("deps", departmentService.getDepByPid(-1));//部门
        map.put("joblevels", jobLevelService.getAllJobLevels());//职称
        map.put("positions", positionService.getAllPos());//职位

        map.put("workID", String.format("%08d", empService.getMaxWorkID() + 1));
        return map;
    }

    //    生成工号
    @GetMapping("/maxWorkID")
    public String getmaxWorkID() {
        return String.format("%08d", empService.getMaxWorkID() + 1);
    }

    @PostMapping("/emp")
    @RequiresPermissions("empAdmin:add")
    public Result addEmp(Employee employee) {
        if (empService.addEmp(employee) == 1) {
            List<Position> poss = positionService.getAllPos();
            for (Position position : poss) {
                if (position.getId() == employee.getPosId()) {
                    employee.setPosName(position.getName());
                }
            }
            executorService.execute(new EmailRunnable(employee, javaMailSender, templateEngine));
            return Result.success("添加成功！");
        }
        return Result.fail("添加失败");
    }

    @PutMapping("/emp")
    @RequiresPermissions("empAdmin:update")
    public Result updateEmp(Employee employee) {
        if (empService.update(employee) == 1) {
            return Result.success("更新成功!");
        }
        return Result.fail("更新失败!");
    }

    @DeleteMapping("/emp/{ids}")
    @RequiresPermissions("empAdmin:delete")
    public Result deleteEmpById(@PathVariable String ids) {
        if (empService.deleteEmpById(ids)) {
            return Result.success("删除成功！");
        }
        return Result.fail("删除失败");
    }

    //    加载所有员工
    @GetMapping("/emp")
    public Map<String, Object> getEmpByPage(@RequestParam(defaultValue = "1", value = "page") Integer page, @RequestParam(defaultValue = "", value = "keywords") String keywords,
                                            @RequestParam(value = "politicId") Integer politicId, @RequestParam(value = "nationId") Integer nationId, @RequestParam("posId") Integer posId,
                                            @RequestParam("jobLevelId") Integer jobLevelId, @RequestParam(value = "engageForm") String engageForm,
                                            @RequestParam("departmentId") Integer departmentId, @RequestParam("beginDateScope") String beginDateScope) {
        HashMap<String, Object> map = new HashMap<>();
        List<Employee> emps = empService.getEmpByPage(page, keywords, politicId, nationId, posId, jobLevelId, engageForm, departmentId, beginDateScope);
        int count = empService.getCountByKeywords(keywords, politicId, nationId, posId, jobLevelId, engageForm, departmentId, beginDateScope);
        map.put("emps", emps);
        map.put("count", count);
        return map;
    }

    @GetMapping("/exportEmp")
    @RequiresPermissions("empAdmin:export")
    public ResponseEntity<byte[]> exportEmp() {
        return ExcelUtils.exportEmp2Excel(empService.getAllEmployees());
    }

    @PostMapping("/importEmp")
    @RequiresPermissions("empAdmin:import")
    public Result importEmp(MultipartFile file) {
        List<Employee> employees = ExcelUtils.importEmp2List(file, empService.getAllNations(),
                empService.getAllPolitics(), departmentService.getAlldeps(),
                positionService.getAllPos(), jobLevelService.getAllJobLevels());
        if (empService.addEmps(employees) == employees.size()) {
            return Result.success("导入成功！");
        }
        return Result.fail("导入失败！");
    }
}
