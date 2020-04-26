package com.lxx.hserver.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Salary {
    private Integer id;
    private Integer bonus;//奖金
    private Integer lunchSalary;//午餐补助
    private Integer basicSalary;//基本工资
    private Integer trafficSalary;//交通补助
    private Integer allSalary;//应发工资；
    private Integer pensionBase;//养老金基数
    private Float pensionPer;//养老金比率
    private Date createDate;
    private Integer medicalBase;//医疗基数
    private Float medicalPer;//医疗比率
    private Integer accumulationFundBase;//公积金基数
    private Float accumulationFundPer;//公积金比率
    private String name;
    private Integer departmentId;
    private  Department department;
}
