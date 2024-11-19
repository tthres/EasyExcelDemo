package com.ithiema.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class employee {
    @ExcelProperty("员工编号")
    private int id;
    @ExcelProperty("员工姓名")
    private String name;
    @ExcelProperty("入职日期")
    private Date date;
    @ExcelProperty("员工工资")
    private double salary;
}
