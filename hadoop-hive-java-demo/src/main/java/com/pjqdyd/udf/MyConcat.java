package com.pjqdyd.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**   
 * @Description:  [Hive中的udf用户自定义函数]
 * 这是一个拼接字符串的函数方法
 * 将这个类打成jar包, 添加到hive中: hive> add jar /tmp/MyConcat.jar
 * 创建临时函数: hive> create temporary function myconcat as 'com.pjqdyd.udf.MyConcat'
 * 查看函数: hive> show functions;
 * 测试函数: hive> select myconcat("test");
 * 删除函数: hive> drop function if exists myconcat;
 * (临时函数只是当前会话有效)
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class MyConcat extends UDF {

    public String evaluate(String word){
        if (word == null){
            return "NULL";
        }

        return word + "_ok";
    }

    public static void main(String[] args){
        System.out.println(new MyConcat().evaluate("test"));
    }
}
