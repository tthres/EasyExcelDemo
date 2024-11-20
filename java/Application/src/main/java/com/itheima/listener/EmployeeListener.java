package com.itheima.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.itheima.domain.Employee;
import com.itheima.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListener implements ReadListener<Employee> {
    private int count = 10000;
    private EmployeeService dao ;
    private List<Employee> list = new ArrayList<>(count);

    public EmployeeListener(EmployeeService dao) {
        this.dao = dao;
    }
    @Override
    public void invoke(Employee employee, AnalysisContext analysisContext) {
        list.add(employee);
        if(list.size()>=count){
            dao.addData(list);
            list = new ArrayList<>(count);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(list.size()>0){
            dao.addData(list);
        }
    }
}
