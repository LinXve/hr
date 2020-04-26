package com.lxx.hserver.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 职位
 */
@Data
@NoArgsConstructor
public class Position {
    private Integer id;
    private String name;
    private Date createDate;
    private Boolean enabled;

    public Position(String name) {
        this.name = name;
    }
}
