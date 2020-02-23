package com.pjqdyd.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;

/**   
 * @Description:  [测试操作Hdfs文件系统]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class HdfsReadTest {

    public static void main(String[] args) throws IOException {
        readFileToConsole("/user/root/input/test.txt");
    }

    //读取hdfs文件系统中的文件
    public static void readFileToConsole(String path) throws IOException {
        //设置配置
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");

        //获取文件系统操作对象
        FileSystem fs = FileSystem.get(conf);

        //具体文件操作
        FSDataInputStream fis = fs.open(new Path(path));

        //打印到控制台
        IOUtils.copyBytes(fis, System.out, 4096, true);
    }
}
