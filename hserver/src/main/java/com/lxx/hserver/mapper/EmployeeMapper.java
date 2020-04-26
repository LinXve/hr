package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.Employee;
import com.lxx.hserver.entity.Nation;
import com.lxx.hserver.entity.PoliticsStatus;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface EmployeeMapper {

    List<Nation> getAllNations();

    List<PoliticsStatus> getAllPolitics();

    Long getMaxWorkID();

    int addEmp(Employee employee);

    int updateEmp(@Param("employee") Employee employee);

    List<Employee> getEmployeesByPage(@Param("start") Integer start, @Param("keywords") String keywords,
                                      @Param("politicId") Integer politicId, @Param("nationId") Integer nationId,
                                      @Param("posId") Integer posId, @Param("jobLevelId") Integer jobLevelId,
                                      @Param("engageForm") String engageForm, @Param("departmentId") Integer departmentId,
                                      @Param("startBeginDate") Date startBeginDate, @Param("endBeginDate") Date endBeginDate);

    int getCountBykeywords(@Param("keywords") String keywords, @Param("politicId") Integer politicId, @Param("nationId") Integer nationId,
                            @Param("posId") Integer posId, @Param("jobLevelId") Integer jobLevelId,
                            @Param("engageForm") String engageForm, @Param("departmentId") Integer departmentId,
                            @Param("startBeginDate") Date startBeginDate, @Param("endBeginDate") Date endBeginDate);

    int deleteEmpById(@Param("ids") String[] split);

    int addEmps(@Param("emps") List<Employee> employees);

    List<Employee> getEmpDepartmentSalary(@Param("page") int page,@Param("keywords") String keywords);
    int getCountByEmpDepSal(@Param("keywords") String keywords);
}
