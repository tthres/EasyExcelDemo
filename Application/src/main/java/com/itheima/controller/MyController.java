package com.itheima.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.itheima.domain.Employee;
import com.itheima.listener.EmployeeListener;
import com.itheima.service.EmployeeService;
import com.itheima.service.impl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/")
public class MyController {
    @Autowired
    private EmployeeService service;

    @RequestMapping("/upload")
    @ResponseBody
    public void upload(MultipartFile file, HttpServletResponse response) throws IOException {
        long t1 = System.currentTimeMillis();
        EasyExcel.read(file.getInputStream(), Employee.class, new EmployeeListener(service)).sheet().doRead();
        long t2 = System.currentTimeMillis();
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().print("导入数据成功, 共用时:"+(t2-t1));
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response) throws IOException {

    }
}
