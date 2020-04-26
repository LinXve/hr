package com.lxx.hserver.utils;

/**
 * 该工具类用于检查String/object是否为空
 * return：true为空，false为非空
 */
public class StringTools {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.equals("") || str.equals("null");
    }
    public static boolean isNullOrEmpty(Object o){
        return o ==null || o.equals("");
    }
}
