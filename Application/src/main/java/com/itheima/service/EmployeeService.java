package com.itheima.service;

import com.itheima.domain.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {
    public List<Employee> getData();
    public void addData(List<Employee> list);
}
