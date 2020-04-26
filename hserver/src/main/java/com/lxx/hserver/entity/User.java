package com.lxx.hserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class User implements Serializable {
    private Integer id;

    private String username;

    @JsonIgnore
    private String password;

    private Boolean state;

    private String name;

    private String userfase;

    private String remark;

    private List<Role> roles;
}