package com.lxx.hserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Department {
    private Integer id;
    private String name;//部门名
    private Integer parentId;//父部门id
    @JsonIgnore
    private String depPath;//部门树序列
    private Boolean enabled;//部门是否禁用
    @JsonIgnore
    private Boolean isParent;//是不是父部门

    private List<Department> childs;
    private Salary salary;
    public Department(String name) {
        this.name = name;
    }
}

