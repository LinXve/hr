package com.lxx.hserver.entity;

import lombok.Data;

/*
民族
 */
@Data
public class Nation {
    private Integer id;
    private String name;

    public Nation(String name) {
        this.name = name;
    }
}
