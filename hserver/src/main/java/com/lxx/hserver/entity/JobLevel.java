package com.lxx.hserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *  职称
 */
@Data
@NoArgsConstructor
public class JobLevel {
    private Integer id;
    private String name;
    private String titleLevel;//职称等级

    private Date createDate;//创建时间
//    @JsonIgnore
    private Boolean enabled;//是否可用

    public JobLevel(String name) {
        this.name = name;
    }
}
