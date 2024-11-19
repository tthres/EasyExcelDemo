import com.alibaba.excel.util.ListUtils;
import com.itheima.domain.Employee;
import com.itheima.mapper.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class MapperTest {

    @Autowired
    private EmployeeMapper dao;

    @Test
    public void testBeachInsert(){
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            List<Employee> data = getData(100);
            dao.beathInsert(data);
       }
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1+"----------------------------");
    }

    @Test
    public void testGetData(){
        long t1 = System.currentTimeMillis();
        System.out.println(dao.getData());
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1+"=======================");
    }

    // 获取测试数据的方法
    private List<Employee> getData(int count) {
        List<Employee> list = ListUtils.newArrayList();
        for (int i = 1; i <= count; i++) {
            Employee data = new Employee(i,"测试"+i,i*6.6,new Date());
            list.add(data);
        }
        return list;
    }
}
