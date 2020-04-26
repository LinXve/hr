package com.lxx.hserver.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class Role implements Serializable {
    private Integer rid;

    private Boolean available;

    private String description;

    private String role;

    private List<Permission> permissions;

}