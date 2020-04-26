package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {
    List<Department> getDepByPid(@Param("pid")Integer did);
    Department getDepById(@Param("id")Integer id);
    int addDep(@Param("department") Department department);

    int deleteDep(Integer id);
}
