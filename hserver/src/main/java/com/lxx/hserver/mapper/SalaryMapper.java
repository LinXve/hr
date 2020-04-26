package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.Salary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalaryMapper {
    int addSalary(@Param("salary") Salary salary);

    List<Salary> getAllSalary();

    int updateSalary(@Param("salary") Salary salary);

    int deleteSalary(@Param("ids") String[] ids);

    int updateDepartmentSalary(@Param(value = "sid") Integer sid, @Param(value = "did") Integer did);
//    插入中间连接表关联
    int addDepartmentSalary(@Param(value = "sid") Integer sid, @Param(value = "did") Integer did);
}
