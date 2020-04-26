package com.lxx.hserver.utils;

import com.lxx.hserver.entity.User;
import com.lxx.hserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

/**
 * 获取当前登录的用户
 */
@Slf4j
public class CurrentUserName {
    public static String getUserName(){
        return (String) SecurityUtils.getSubject().getPrincipal();
    }
}
