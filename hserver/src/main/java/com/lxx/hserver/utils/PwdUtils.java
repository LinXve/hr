package com.lxx.hserver.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 通用加密：simpleHash，()
 */
public class PwdUtils {
    private static final int hashIterations=1024;//加密解密次数
    public static void main(String[] args) {
        String admin = getPwd("123");
        System.out.println(admin);
    }

    public static String getPwd(String pwd) {
        return new SimpleHash("MD5", pwd, ByteSource.Util.bytes("salt"), hashIterations).toHex();
    }
}
