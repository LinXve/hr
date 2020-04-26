package com.lxx.hserver.controller.sys;

import com.lxx.hserver.entity.Department;
import com.lxx.hserver.entity.JobLevel;
import com.lxx.hserver.entity.Permission;
import com.lxx.hserver.entity.Position;
import com.lxx.hserver.service.*;
import com.lxx.hserver.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/sys/basic")
@RequiresRoles("admin")
public class SysBasicController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private JobLevelService jobLevelService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
//  role
    @GetMapping("/roles")
    public Result getAllRoles(){
        return Result.success(roleService.allRoles());
    }
    @DeleteMapping("/role/{rid}")
    public Result deleteRole(@PathVariable("rid")Integer rid){
        if (roleService.deleteRoleById(rid)==1){
            return Result.success("删除成功！");
        }
        return Result.fail("删除失败!");
    }
    @DeleteMapping("/deleteRolePermission")
    public Result deleteRolePermission(@RequestParam("rid")Integer rid,@RequestParam("pid")Integer pid){
        if (roleService.deleteRolePermission(rid,pid)==1){
            return Result.success("删除成功！");
        }
        return Result.fail("删除失败！");
    }
    @PostMapping("/addRole")
    public Result addRole(@RequestParam("role") String role,@RequestParam("description")String description){
        if (roleService.addRole(role,description)==1){
            return Result.success("添加新角色成功!");
        }
        return Result.fail("添加失败!");
    }
    @GetMapping("/permissionTree")
    public Result getPermissions(@RequestParam("rid")Integer rid){
        List<Permission> allPermission = permissionService.getAllPermission(rid);
        return Result.success(allPermission);
    }
    @PostMapping("/addPermissions")
    public Result addPermissions(@RequestParam("rid")Integer rid,@RequestParam("pids")String []pids){
        if (pids.length>0 &&this.roleService.addRolePermission(rid,pids)==pids.length){
            return Result.success("添加成功!");
        }
        return Result.fail("添加失败!");
    }
    //    职称
    @GetMapping("/allJobLevel")
    public Result getAllJobLevel() {
        return Result.success(jobLevelService.getAllJobLevelWithBasic());
    }

    @PostMapping("/addJobLevel")
    public Result AddJobLevel(JobLevel jobLevel) {
        int i = jobLevelService.addJobLevel(jobLevel);
        if (i == 1) {
            return Result.success("新增职称成功！");
        } else {
            return Result.fail("添加失败,該用戶已存在！");
        }
    }

    @PutMapping("/jobLevel")
    public Result upDateJobLevel(JobLevel jobLevel) {
        if (jobLevelService.updateJobLevel(jobLevel) == 1) {
            return Result.success("修改成功！");
        }
        return Result.fail("更新失败,该职称已存在");
    }

    @PutMapping("/JLEnabled")
    public Result jobLevelEnabled(JobLevel jobLevel) {
        if (jobLevelService.jobLevelEnabled(jobLevel) == 1) {
            return Result.success("更新成功!");
        }
        return Result.fail("更新失败！");
    }

    @DeleteMapping("/joblevel")
    public Result deleteJL(@RequestParam("ids") String ids) {
        if (jobLevelService.deleteJobLevel(ids)) {
            return Result.success("删除成功！");
        }
        return Result.fail("删除失败！");
    }

    //    职位
    @GetMapping("/allPos")
    public Result getAllPos() {
        return Result.success(positionService.getPos());
    }

    @PostMapping("/pos/{posName}")
    public Result AddPos(@PathVariable("posName") String posName) {
        int i = positionService.addPos(posName);
        if (i == 1) {
            return Result.success("添加职位成功！");
        } else if (i == 0)
            return Result.fail("添加失败，该职位已存在！");
        return Result.fail("添加失败！");
    }

    @PutMapping("/disabled")
    public Result disabledPos(Position position) {
        if (positionService.disabledPos(position) == 1) {
            return Result.success("更新成功！");
        }
        return Result.fail("更新失败！");
    }

    @PutMapping("/pos")
    public Result updatePos(Position position) {
        if (positionService.updatePos(position) == 1) {
            return Result.success("更新成功！");
        }
        return Result.fail("更新失败,该职位已存在");
    }

    @DeleteMapping("pos")
    public Result deleteManyPos(@RequestParam("ids") String ids) {
        if (positionService.deletePosByIds(ids)) {
            return Result.success("删除成功！");
        }
        return Result.fail("删除失败!");
    }

    //    获取所有的部门列表
    @GetMapping("/deps")
    public Result getAllDeps() {
        return Result.success(departmentService.getAlldeps());
    }

    //    获取部门tree
    @RequestMapping(value = "/dep/{pid}", method = RequestMethod.GET)
    public Result getDepByPid(@PathVariable(value = "pid") Integer pid) {
        return Result.success(departmentService.getDepByPid(pid));
    }

    //    添加部门
    @RequestMapping(value = "/dep", method = RequestMethod.POST)
    public Result addDep(Department department) {
        if (departmentService.addDep(department) == 1) {
            return Result.success(department);
        }
        return Result.fail("添加失败！");
    }

    //    删除部门
    @DeleteMapping("/dep/{did}")
    public Result deleteDep(@PathVariable("did") Integer did) {
        if (departmentService.deleteDepById(did) == 1) {
            return Result.success("删除成功!");
        }
        return Result.fail("删除失败!");
    }
}
