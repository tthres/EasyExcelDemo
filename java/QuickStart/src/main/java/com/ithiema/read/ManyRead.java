package com.ithiema.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.ithiema.EmployeeListener.EmployeeListener;
import com.ithiema.dao.EmployeeDao;
import com.ithiema.pojo.employee;
import com.ithiema.utils.TestFileUtil;
import org.junit.Test;

public class ManyRead {
    @Test
    public void read() {
        String fileName = TestFileUtil.getPath() + "simpleWrite1732025240352.xlsx";
        ExcelReader reader = EasyExcel.read(fileName, employee.class, new EmployeeListener(new EmployeeDao())).build();
        ReadSheet sheet = EasyExcel.readSheet().build();
        reader.read(sheet);
    }
}
