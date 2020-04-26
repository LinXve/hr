package com.lxx.hserver.config;

import com.alibaba.fastjson.JSONObject;
import com.lxx.hserver.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义session manager 。每次请求记住sessionId
 */
@Slf4j
public class MySessionManager extends DefaultWebSessionManager {
    private static final String AUTHORIZATION="Authorization";
    private static final String REFERENCED_SESSION_ID_SOURSE="Stateless request";

    public MySessionManager() {
        super();
    }

    /**
     * @param request
     * @param response
     * @return
     * 前端请求域中保存sessionId（token）。让后台知道每次请求都是同一个人
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String token= WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        if (!StringUtils.isEmpty(token)){
//            如果请求头中有Authorization，则sessionId为其值
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,REFERENCED_SESSION_ID_SOURSE);//sessionid来源
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,token);//sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,Boolean.TRUE);
            log.info("Authorization：" + token);
            return token;
        }else {
            return super.getSessionId(request,response);
        }
    }
}
