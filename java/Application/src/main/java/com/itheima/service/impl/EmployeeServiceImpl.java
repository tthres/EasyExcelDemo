package com.itheima.service.impl;

import com.itheima.domain.Employee;
import com.itheima.mapper.EmployeeMapper;
import com.itheima.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper dao;

    @Override
    public List<Employee> getData() {
        return dao.getData();
    }

    @Override
    public void addData(List<Employee> list) {
        dao.beathInsert(list);
    }
}
