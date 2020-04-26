package com.lxx.hserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lxx.hserver.mapper")
public class HserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(HserverApplication.class, args);
    }

}
