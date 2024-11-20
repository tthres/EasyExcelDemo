package com.ithiema.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.ithiema.pojo.employee;
import com.ithiema.utils.TestFileUtil;
import org.junit.Test;

public class SimpleReader {

    @Test
    public void read() {
        String fileName = TestFileUtil.getPath() + "simpleWrite1731950180841.xlsx";
        EasyExcel.read(fileName, employee.class, new PageReadListener<employee>(dataList -> {
            for (employee employee : dataList) {
                System.out.println(employee);
            }
        })).sheet().doRead();
    }
}
