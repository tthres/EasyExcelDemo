package com.ithiema.dao;

import com.ithiema.pojo.employee;

import java.util.List;

public class EmployeeDao {
    public void save(List<employee> list) {
        System.out.println(list.size()+"模拟操作数据库");
    }
}
