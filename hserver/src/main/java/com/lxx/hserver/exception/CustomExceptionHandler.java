package com.lxx.hserver.exception;

import com.lxx.hserver.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@ControllerAdvice
@ResponseBody
@Slf4j
public class CustomExceptionHandler {
    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result error(HttpServletRequest request, Exception e){
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result error(HttpServletRequest request,UnauthorizedException e){
        Principal userPrincipal = request.getUserPrincipal();
        return Result.fail(500,"权限不足！",e.getMessage());
    }
}
