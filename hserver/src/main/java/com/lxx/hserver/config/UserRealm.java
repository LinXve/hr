package com.lxx.hserver.config;

import com.lxx.hserver.entity.Permission;
import com.lxx.hserver.entity.Role;
import com.lxx.hserver.entity.User;
import com.lxx.hserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("授权认证");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = userService.findByName((String) principalCollection.getPrimaryPrincipal());
        log.info(user.toString());
//        使用注解形式，只有赋予了的角色、权限都可以在这里授权
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            info.addRole(role.getRole());//赋予角色
            List<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                info.addStringPermission(permission.getPermission());//授予权限
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        log.info("身份认证");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findByName(token.getUsername());
        if (user == null) {
            return null;
        } else if (!user.getState()) {
//            false：账户冻结
            throw new LockedAccountException();
        }
        SecurityUtils.getSubject().getSession().setAttribute("username", user.getUsername());
        ByteSource salt = ByteSource.Util.bytes("salt");
        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), salt, getName());
    }
}
