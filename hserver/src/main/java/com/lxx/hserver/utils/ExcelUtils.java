package com.lxx.hserver.utils;

import com.lxx.hserver.entity.*;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成文档
 */
public class ExcelUtils {
    //    导出员工表
    public static ResponseEntity<byte[]> exportEmp2Excel(List<Employee> emps) {
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        try {
//            1.创建Excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
//            2.创建文档摘要
            workbook.createInformationProperties();
//            3.获取文档信息，并配置
            DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
            dsi.setCategory("员工信息表");//文档类别
            dsi.setCompany("XXX公司");//组织机构
//            4.获取摘要信息并配置
            SummaryInformation si = workbook.getSummaryInformation();
            si.setSubject("员工信息表");//文档主题
            si.setTitle("员工信息表");//标题
            si.setAuthor("XXX公司");//文档作者
//            创建Excel表单
            HSSFSheet sheet = workbook.createSheet();
//            日期显示格式
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
//          5.设置表头
            HSSFRow headRow = sheet.createRow(0);
            HSSFCell cell0 = headRow.createCell(0);
            cell0.setCellValue("编号");
            HSSFCell cell1 = headRow.createCell(1);
            cell1.setCellValue("姓名");
            HSSFCell cell2 = headRow.createCell(2);
            cell2.setCellValue("工号");
            HSSFCell cell3 = headRow.createCell(3);
            cell3.setCellValue("性别");
            HSSFCell cell4 = headRow.createCell(4);
            cell4.setCellValue("出生日期");
            HSSFCell cell5 = headRow.createCell(5);
            cell5.setCellValue("身份证");
            HSSFCell cell6 = headRow.createCell(6);
            cell6.setCellValue("婚姻");
            HSSFCell cell7 = headRow.createCell(7);
            cell7.setCellValue("民族");
            HSSFCell cell8 = headRow.createCell(8);
            cell8.setCellValue("籍贯");
            HSSFCell cell9 = headRow.createCell(9);
            cell9.setCellValue("政治面貌");
            HSSFCell cell10 = headRow.createCell(10);
            cell10.setCellValue("电话");
            HSSFCell cell11 = headRow.createCell(11);
            cell11.setCellValue("联系地址");
            HSSFCell cell12 = headRow.createCell(12);
            cell12.setCellValue("所属部门");
            HSSFCell cell13 = headRow.createCell(13);
            cell13.setCellValue("职称");
            HSSFCell cell14 = headRow.createCell(14);
            cell14.setCellValue("职位");
            HSSFCell cell15 = headRow.createCell(15);
            cell15.setCellValue("聘用形式");
            HSSFCell cell16 = headRow.createCell(16);
            cell16.setCellValue("最高学历");
            HSSFCell cell17 = headRow.createCell(17);
            cell17.setCellValue("专业");
            HSSFCell cell18 = headRow.createCell(18);
            cell18.setCellValue("毕业学校");
            HSSFCell cell19 = headRow.createCell(19);
            cell19.setCellValue("入职日期");
            HSSFCell cell20 = headRow.createCell(20);
            cell20.setCellValue("在职情况");
            HSSFCell cell21 = headRow.createCell(21);
            cell21.setCellValue("邮箱");
            HSSFCell cell22 = headRow.createCell(22);
            cell22.setCellValue("合同期限（/年）");
            HSSFCell cell23 = headRow.createCell(23);
            cell23.setCellValue("合同起始日期");
            HSSFCell cell24 = headRow.createCell(24);
            cell24.setCellValue("合同终止日期");

//            6.装数据
            for (int i = 0; i < emps.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                Employee emp = emps.get(i);
                row.createCell(0).setCellValue(emp.getId());
                row.createCell(1).setCellValue(emp.getName());
                row.createCell(2).setCellValue(emp.getWorkID());
                row.createCell(3).setCellValue(emp.getGender());
                HSSFCell birthdayCell = row.createCell(4);
                birthdayCell.setCellValue(emp.getBirthday());
                row.createCell(5).setCellValue(emp.getIdCard());
                row.createCell(6).setCellValue(emp.getWedlock());
                row.createCell(7).setCellValue(emp.getNation().getName());
                row.createCell(8).setCellValue(emp.getNativePlace());
                row.createCell(9).setCellValue(emp.getPoliticsStatus().getName());
                row.createCell(10).setCellValue(emp.getPhone());
                row.createCell(11).setCellValue(emp.getAddress());
                row.createCell(12).setCellValue(emp.getDepartment().getName());
                row.createCell(13).setCellValue(emp.getJobLevel().getName());
                row.createCell(14).setCellValue(emp.getPosition().getName());
                row.createCell(15).setCellValue(emp.getEngageForm());
                row.createCell(16).setCellValue(emp.getTiptopDegree());
                row.createCell(17).setCellValue(emp.getSpecialty());
                row.createCell(18).setCellValue(emp.getSchool());
                HSSFCell beginDateCell = row.createCell(19);
                beginDateCell.setCellValue(emp.getBeginDate());
                row.createCell(20).setCellValue(emp.getWorkState());
                row.createCell(21).setCellValue(emp.getEmail());
                row.createCell(22).setCellValue(emp.getContractTerm());
                HSSFCell beginContractCell = row.createCell(23);
                beginContractCell.setCellValue(emp.getBeginContract());
                HSSFCell endContractCell = row.createCell(24);
                endContractCell.setCellValue(emp.getEndContract());
            }
            headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", new String("员工信息表.xls".getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            baos = new ByteArrayOutputStream();
            workbook.write(baos);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    /**
     * 导入
     * MultipartFile  这个类一般是用来接受前台传过来的文件
     */
    public static List<Employee> importEmp2List(MultipartFile file,
                                                List<Nation> allNations,
                                                List<PoliticsStatus> allPolitics,
                                                List<Department> allDeps,
                                                List<Position> allPos,
                                                List<JobLevel> allJobLevels) {
        List<Employee> emps = new ArrayList<>();
        try {
            HSSFWorkbook workbook =
                    new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                Employee employee;
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    if (j == 0) {
                        continue;//标题行
                    }
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//没数据
                    }
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    employee = new Employee();
                    for (int k = 0; k < physicalNumberOfCells; k++) {
                        HSSFCell cell = row.getCell(k);
                        switch (cell.getCellTypeEnum()) {
                            case STRING: {
                                String cellValue = cell.getStringCellValue();
                                if (cellValue == null) {
                                    cellValue = "";
                                }
                                switch (k) {
                                    case 1:
                                        employee.setName(cellValue);
                                        break;
                                    case 2:
                                        employee.setWorkID(cellValue);
                                        break;
                                    case 3:
                                        employee.setGender(cellValue);
                                        break;
                                    case 5:
                                        employee.setIdCard(cellValue);
                                        break;
                                    case 6:
                                        employee.setWedlock(cellValue);
                                        break;
                                    case 7:
                                        int nationIndex = allNations.indexOf(new Nation(cellValue));
                                        employee.setNationId(allNations.get(nationIndex).getId());
                                        break;
                                    case 8:
                                        employee.setNativePlace(cellValue);
                                        break;
                                    case 9:
                                        int psIndex = allPolitics.indexOf(new PoliticsStatus(cellValue));
                                        employee.setPoliticId(allPolitics.get(psIndex).getId());
                                        break;
                                    case 10:
                                        employee.setPhone(cellValue);
                                        break;
                                    case 11:
                                        employee.setAddress(cellValue);
                                        break;
                                    case 12:
                                        int depIndex = allDeps.indexOf(new Department(cellValue));
                                        employee.setDepartmentId(allDeps.get(depIndex).getId());
                                        break;
                                    case 13:
                                        int jlIndex = allJobLevels.indexOf(new JobLevel(cellValue));
                                        employee.setJobLevelId(allJobLevels.get(jlIndex).getId());
                                        break;
                                    case 14:
                                        int posIndex = allPos.indexOf(new Position(cellValue));
                                        employee.setPosId(allPos.get(posIndex).getId());
                                        break;
                                    case 15:
                                        employee.setEngageForm(cellValue);
                                        break;
                                    case 16:
                                        employee.setTiptopDegree(cellValue);
                                        break;
                                    case 17:
                                        employee.setSpecialty(cellValue);
                                        break;
                                    case 18:
                                        employee.setSchool(cellValue);
                                        break;
                                    case 19:
                                    case 20:
                                        employee.setWorkState(cellValue);
                                        break;
                                    case 21:
                                        employee.setEmail(cellValue);
                                        break;
                                }
                            }
                            break;
                            default: {
                                switch (k) {
                                    case 4:
                                        employee.setBirthday(cell.getDateCellValue());
                                        break;
                                    case 19:
                                        employee.setBeginDate(cell.getDateCellValue());
                                        break;
                                    case 22:
                                        employee.setContractTerm(cell.getNumericCellValue());
                                        break;
                                    case 23:
                                        employee.setBeginContract(cell.getDateCellValue());
                                        break;
                                    case 24:
                                        employee.setEndContract(cell.getDateCellValue());
                                        break;
                                }
                            }
                            break;
                        }
                    }
                    emps.add(employee);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emps;
    }
}
