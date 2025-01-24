package com.ithiema.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.ithiema.pojo.employee;
import com.ithiema.utils.TestFileUtil;
import lombok.Data;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class SimpleWrite {
    private List<employee> data(int count) {
        List<employee> list = ListUtils.newArrayList();
        for (int i = 1; i <= count; i++) {
            list.add(new employee(i, "测试数据"+i, new Date(), 6.6*i, "货主"+i, i*1.1, "货主"+i, "货主"+i, "货主"+i, "货主"+i, "货主"+i, "货主"+i, "货主"+i, "货主"+i, "货主"+i, "货主"+i));
        }
        return list;
    }
    @Test
    public void write() {
        String fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName, employee.class).sheet("aaa").doWrite(data(10));
    }
}
