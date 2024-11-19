package com.itheima.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    // 成员变量
    @ExcelProperty("员工工号")
    private int id;
    @ExcelProperty("员工姓名")
    private String name;
    @ExcelProperty("员工工资")
    private double salary;
    @ExcelProperty("入职日期")
    private Date date;

}
