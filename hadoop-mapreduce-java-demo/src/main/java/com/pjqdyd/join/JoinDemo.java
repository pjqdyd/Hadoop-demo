package com.pjqdyd.join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**   
 * @Description:  [多表联查案例]
 * 数据:
 * 登录表loginTable:
 * 1    2020-1-2
 * 2    2020-2-3
 * 3    2020-3-1
 * 用户表userTable:
 * 1    张三
 * 2    李四
 * 3    王五
 * 4    小明
 *
 * 结果:
 * 1    张三  2020-1-2
 * 2    李四  2020-2-3
 * 3    王五  2020-3-1
 * 4    小明  "未登录"
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class JoinDemo {

    //启动主方法
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        //1. 获取配置对象, 配置conf
        Configuration conf = new Configuration();
        //2. 获取job对象, 设置job运行的主类
        Job job = Job.getInstance(conf, "join-demo");
        job.setJarByClass(JoinDemo.class);
        //3. 对map阶段设置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0])); //从控制台接收第一个参数 (user表文件)

        //设置缓存数据
        job.addCacheFile(new URI(args[1])); //这里应该是loginTable文件的路径

//        //4. 对reduce阶段设置
//        job.setReducerClass(MyReducer.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        //5. 提交job
        int isok = job.waitForCompletion(true) ? 0 : 1;
        //退出job, 并打印信息
        System.exit(isok);
    }

    //map端的join是: 将小表进行分布式缓存, 然后在map端取出数据进行连接查询, (适合小表操作)
    public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {
        //定义一个map集合来存储文件中的内容
        public Map<String, String> loginMap = new ConcurrentHashMap<>();

        //执行一次,先读取掉登录文件
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            //首先读取缓存数据
            Path[] paths = context.getLocalCacheFiles();
            for (Path path : paths){
                String fileName = path.getName();
                BufferedReader br = null;
                String line = null;
                if (fileName.equals("loginTable")){ //将登录表加载到集合
                    br = new BufferedReader(new FileReader(new File(path.toString())));
                    while ((line = br.readLine()) != null){
                        String[] strs = line.split("\t");
                        loginMap.put(strs[0], strs[1]); //数据存入map集合中
                    }
                    br.close();
                }
            }

        }

        //这里传入的就是用户文件
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\t");
            if (!loginMap.containsKey(fields[0])){ //如果id不在map集合中, 就不进行连接操作
                context.write(new Text(fields[0] + "\t" + fields[1] + "\t" + "未登录"), new Text(""));
            }
            context.write(new Text(fields[0] + "\t" + fields[1] + "\t" + loginMap.get(fields[0])), new Text(""));
        }
    }

//    public static class MyReducer extends Reducer<Text, Text, Text, Text> {
//
//        @Override
//        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//
//        }
//    }

}
