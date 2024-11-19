package com.itheima.utils;
/*
    获取代码路径的工具类，可以获取到模块这一级的磁盘路径；
 */
public class TestFileUtil {
    public static String getPath() {
        return TestFileUtil.class.getResource("/").getPath().replace("classes/","");
    }

    public static void main(String[] args) {
        System.out.println(getPath());
    }
}
