package com.pjqdyd.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**   
 * @Description:  [测试操作Hdfs文件系统]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class HdfsTest1 {

    public static void main(String[] args) throws IOException {
        readFileToLocal("/user/root/input/test.txt");
    }

    //读取hdfs文件系统中的文件到本地
    public static void readFileToLocal(String path) throws IOException {
        FSDataInputStream fis = null;
        OutputStream  out = null;
        try {
            //设置配置
            Configuration conf = new Configuration();
            //conf.set("fs.defaultFS", "hdfs://localhost:9000");
            //获取文件系统操作对象
            FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf, "root");

            //具体文件操作
            fis = fs.open(new Path(path));
            out = new FileOutputStream(new File("C:\\Users\\Z2\\Desktop\\hadoop\\test.txt"));

            //拷贝到输出流文件中
            IOUtils.copyBytes(fis, out, 4096, true);
        } catch (Exception e) {
        } finally {
            fis.close();
            out.close();
        }
    }
}
