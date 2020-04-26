package com.lxx.hserver.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * 统一结果返回
 */
@Data
@AllArgsConstructor
public class Result {
    private Integer status;//状态码，200:success, 500:error
    private String msg; //返回信息
    private  Object obj; //保存返回数据

    public static Result success(String msg,Object object){
        return new Result(200,msg,object);
    }
    public static Result success(String msg){
        return new Result(200,msg,null);
    }
    public static Result success(){
        return new Result(200,null,null);
    }
    public static Result success(Object o){
        return new Result(200,null,o);
    }
    public static Result fail(String msg,Object object){
        return new Result(500,msg,object);
    }
    public static Result fail(String msg){
        return new Result(500,msg,null);
    }
    public static Result fail(){
        return new Result(500,null,null);
    }
    public static Result fail(Integer status,String msg,Object o){
        return new Result(status,msg,o);
    }
}
