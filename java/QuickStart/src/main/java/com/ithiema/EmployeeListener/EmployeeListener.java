package com.ithiema.EmployeeListener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.ithiema.dao.EmployeeDao;
import com.ithiema.pojo.employee;

import java.util.ArrayList;

public class EmployeeListener implements ReadListener<employee> {

    private ArrayList<employee> list = new ArrayList<>();
    private int count = 100;
    private EmployeeDao dao;

    public EmployeeListener(EmployeeDao dao) {
        this.dao = dao;
    }

    @Override
    public void invoke(employee employee, AnalysisContext analysisContext) {
        list.add(employee);
        if (list.size() >= count) {
            dao.save(list);
            list = new ArrayList<>(count);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (list.size() > 0) {
            dao.save(list);
            list = new ArrayList<>(count);
        }
    }
}
