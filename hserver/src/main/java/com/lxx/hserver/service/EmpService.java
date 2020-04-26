package com.lxx.hserver.service;

import com.lxx.hserver.entity.Employee;
import com.lxx.hserver.entity.Nation;
import com.lxx.hserver.entity.PoliticsStatus;
import com.lxx.hserver.mapper.EmployeeMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmpService {
    @Autowired
    private EmployeeMapper employeeMapper;

    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");//数据格式化

    public List<Nation> getAllNations() {
        return employeeMapper.getAllNations();
    }

    public List<PoliticsStatus> getAllPolitics() {
        return employeeMapper.getAllPolitics();
    }

    public Long getMaxWorkID() {
        return employeeMapper.getMaxWorkID();
    }

    public int addEmp(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        Double contractTerm = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12
                + (Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract)));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(contractTerm / 12)));
        return employeeMapper.addEmp(employee);
    }

    public int update(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        Double contractTerm = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12 + Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(contractTerm / 12)));
        return employeeMapper.updateEmp(employee);
    }

    public List<Employee> getEmpByPage(Integer page, String keywords, Integer politicId, Integer nationId, Integer posId, Integer jobLevelId, String engageForm, Integer departmentId, String beginDateScope) {
        Date startBeginDate = null;
        Date endBeginDate = null;
        if (beginDateScope != null && beginDateScope.contains(",")) {
            try {
                String[] split = beginDateScope.split(",");
                startBeginDate = birthdayFormat.parse(split[0]);
                endBeginDate = birthdayFormat.parse(split[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return employeeMapper.getEmployeesByPage((page - 1)*10, keywords, politicId, nationId, posId, jobLevelId, engageForm, departmentId, startBeginDate, endBeginDate);
    }

    public int getCountByKeywords(String keywords, Integer politicId, Integer nationId, Integer posId, Integer jobLevelId, String engageForm, Integer departmentId, String beginDateScope) {
        Date startBeginDate = null;
        Date endBeginDate = null;
        if (beginDateScope != null && beginDateScope.contains(",")) {
            try {
                String[] split = beginDateScope.split(",");
                startBeginDate = birthdayFormat.parse(split[0]);
                endBeginDate = birthdayFormat.parse(split[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return employeeMapper.getCountBykeywords(keywords, politicId, nationId, posId, jobLevelId, engageForm, departmentId, startBeginDate, endBeginDate);
    }

    public boolean deleteEmpById(String ids) {
        String[] split = ids.split(",");
        return employeeMapper.deleteEmpById(split) == split.length;
    }

    public List<Employee> getAllEmployees() {
        return employeeMapper.getEmployeesByPage(null,null,null,null,null,null,null,null,null,null);
    }

    public int addEmps(List<Employee> employees) {
        return employeeMapper.addEmps(employees);
    }

    public List<Employee> getEmpAndSalaryByPage(int page,String keywords) {
        int start=(page-1)*10;
        return employeeMapper.getEmpDepartmentSalary(start,keywords);
    }
    public int getCountByEmpDepSal(String keywords){
        return employeeMapper.getCountByEmpDepSal(keywords);
    }
}
