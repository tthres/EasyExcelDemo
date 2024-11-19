package com.itheima.mapper;

import com.itheima.domain.Employee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {
    public void beathInsert(@Param("list") List<Employee> list);

    @Select("select * from employee ")
    @ResultType(Employee.class)
    List<Employee> getData();

}
