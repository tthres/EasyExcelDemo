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

    @ExcelProperty("货主")
    private String load;

    @ExcelProperty("柜号")
    private double cNum;

    @ExcelProperty("地址")
    private String address;

    @ExcelProperty("test1")
    private String test1;

    @ExcelProperty("test2")
    private String test2;

    @ExcelProperty("test3")
    private String test3;

    @ExcelProperty("test4")
    private String test4;

    @ExcelProperty("test5")
    private String test5;

    @ExcelProperty("test6")
    private String test6;

    @ExcelProperty("test7")
    private String test7;

    @ExcelProperty("test8")
    private String test8;

    @ExcelProperty("test9")
    private String test9;
}
