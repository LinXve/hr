package com.lxx.hserver.controller.sys;

import com.lxx.hserver.entity.User;
import com.lxx.hserver.mapper.UserMapper;
import com.lxx.hserver.service.UserService;
import com.lxx.hserver.utils.Result;
import com.lxx.hserver.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/get")
    @RequiresRoles("admin")
    public Result getUsersByKeywords(@RequestParam(value = "keywords")String keywords){
        if(keywords==""&&keywords==null){
            return Result.success(userService.getUserByKeyWords(null));
        }
        return Result.success(userService.getUserByKeyWords(keywords));
    }

    @GetMapping("/id/{id}")
    public Result getUser(@RequestParam("id") Integer id){
        User user = userService.select(id);
        if (!StringTools.isNullOrEmpty(user)){
            return Result.success(user);
        }else {
            return Result.fail("查无该用户！");
        }
    }
    @DeleteMapping("/{id}")
    @RequiresRoles(value = "admin")
    public Result deleteUserById(@PathVariable("id") Integer id){
        if (userService.deleteById(id)){
            return Result.success("删除成功");
        }
        else
            return Result.fail("删除失败");
    }
    @PutMapping("/")
    @RequiresRoles("admin")
    public Result update(User user){
        if (userService.update(user)==1){
            return Result.success("更新用户信息成功！");
        }else
            return Result.fail("更新失败！");
    }
    @PutMapping("/roles")
    @RequiresRoles("admin")
    public Result updateUserRoles(@RequestParam("uid") Integer uid,@RequestParam("rids") Integer[] rids){
        if (userService.updateUserRoles(uid,rids)==rids.length){
            return Result.success("更新成功！");
        }else
            return Result.fail("更新失败！");
    }

    @PostMapping("/UserReg")
    public Result registerUser(@RequestParam("username")String username,@RequestParam("password") String password){
        if (userService.register(username,password)==1){
            return Result.success("注册成功！");
        }else
            return Result.fail("注册失败,该用户名已存在！");
    }
}
