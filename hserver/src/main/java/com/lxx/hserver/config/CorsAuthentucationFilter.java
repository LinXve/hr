package com.lxx.hserver.config;

import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 解决跨域问题
 */
@Slf4j
public class CorsAuthentucationFilter extends FormAuthenticationFilter {
    public CorsAuthentucationFilter() {
        super();
    }
    //    过滤options预请求,重写父方法
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest){
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")){
                log.info("options预请求");
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse servletResponse= (HttpServletResponse) response;
        servletResponse.setHeader("Access-Control-Allow-Origin","*");
        servletResponse.setStatus(HttpServletResponse.SC_OK);//200
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("text/html;charset=UTF-8");
        PrintWriter writer=servletResponse.getWriter();
        Map<String,Object> map=new HashMap<>();
        map.put("status",500);
        map.put("msg","未登录");
        writer.write(JSONUtils.toJSONString(map));
        writer.close();
        return false;
    }
}
