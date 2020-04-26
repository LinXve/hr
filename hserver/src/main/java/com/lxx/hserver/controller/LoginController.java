package com.lxx.hserver.controller;

import com.lxx.hserver.entity.User;
import com.lxx.hserver.service.UserService;
import com.lxx.hserver.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class LoginController {
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public Result login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) {
        log.info(username + " " + password);
        Subject subject = SecurityUtils.getSubject();
        if (username != null && password != null) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try {
                subject.login(token);
               String principal = (String) subject.getPrincipal();
               log.info(principal);
                User byName = userService.findByName(principal);
                return Result.success("登录成功!", byName);
            } catch (AuthenticationException e) {
                log.error(token.getUsername() + " " + token.getPassword());
                throw new AuthenticationException("用户名或密码错误，请重新输入");
            }
        } else {
            return Result.fail("用户名和密码不能为空！");
        }
    }


    @RequiresPermissions("admin:get")
    @GetMapping("/getuser")
    public Result getUser() {
        User user = userService.select(2);
        return Result.success("成功获取", user);
    }
    @RequestMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.success("退出登录成功！");
    }
}
