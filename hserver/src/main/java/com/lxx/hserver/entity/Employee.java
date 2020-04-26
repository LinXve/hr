package com.lxx.hserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class Employee {
    private Integer id;
    private String name;//员工名
    private String gender;//性别
    private Date birthday;//出生日期
    private String idCard;//身份证
    private String wedlock;//婚姻状况
    private String nativePlace;//籍贯
    private String email;
    private String phone;
    private String address;

    private String tiptopDegree;//最高学历
    private String specialty;//所读专业
    private String school;//所读学校
    private Date beginDate;//入职日期
    private String workState;//现职业状态

    private Double contractTerm;//合同期限
    private Date conversionTime;//转正日期
    private Date notWorkDate;//离职日期
    private Date beginContract;//合同起始日期
    private Date endContract;//合同截至日期
    private String engageForm;//聘用形式

    private String workID;//工号
    private Integer workAge;//工龄

    @JsonIgnore
    private  Integer departmentId;
    @JsonIgnore
    private String departmentName;
    @JsonIgnore
    private Integer jobLevelId;//职称id
    @JsonIgnore
    private Integer posId;//职位id
    private String posName;//职位名
    @JsonIgnore
    private Integer nationId; //民族id
    @JsonIgnore
    private Integer politicId;//政治面貌


    private Department department;//所在部门
    private PoliticsStatus politicsStatus;//政治面貌
    private JobLevel jobLevel;//职称
    private Nation nation;//民族
    private Position position;//职位
}
