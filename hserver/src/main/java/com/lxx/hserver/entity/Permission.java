package com.lxx.hserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;


@Data
public class Permission implements Serializable {
    private Integer pid;

    private String name;

    private String permission;

    @JsonIgnore
    private String url;

    private String path;

}