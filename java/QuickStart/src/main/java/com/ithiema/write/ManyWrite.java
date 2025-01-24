package com.ithiema.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.ithiema.pojo.employee;
import com.ithiema.utils.TestFileUtil;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class ManyWrite {
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
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, employee.class).build();) {
            WriteSheet writeSheet = EasyExcel.writerSheet("测试数据").build();
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 100; i++)
            {
                List<employee> list = data(10000);
                excelWriter.write(list, writeSheet);
            }
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1);
        }
    }
}
