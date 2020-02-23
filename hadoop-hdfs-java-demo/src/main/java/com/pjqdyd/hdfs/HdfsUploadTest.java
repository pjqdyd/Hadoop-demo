package com.pjqdyd.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 *    
 *
 * @Description:  [测试操作Hdfs文件系统]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 *  
 */

public class HdfsUploadTest {

    public static void main(String[] args) throws Exception {
        upFileToHdfs("C:\\Users\\Z2\\Desktop\\hadoop\\test1.txt", "/user/root/input/test1.txt");
    }

    //将文件上传到hdfs系统中
    public static void upFileToHdfs(String filePath, String hdfsPath) throws Exception{
        //设置配置
        Configuration conf = new Configuration();
        //获取文件系统操作对象
        FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf, "root");

        //具体文件操作
        fs.copyFromLocalFile(new Path(filePath), new Path(hdfsPath));
        System.out.println("上传成功!");
    }
}
