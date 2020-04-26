package com.lxx.hserver.entity;


import lombok.Data;

/**
 * 政治面貌
 */
@Data

public class PoliticsStatus {
    private Integer id;
    private String name;

    public PoliticsStatus(String name) {
        this.name = name;
    }
}
