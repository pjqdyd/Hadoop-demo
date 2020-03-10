package com.pjqdyd.param;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**   
 * @Description:  [参数传递方式]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class ParamDemo {

    //启动主方法
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取配置对象, 配置conf
        Configuration conf = new Configuration();
        //读取配置文件的参数
        conf.addResource(ParamDemo.class.getResourceAsStream("./param.xml"));
        //设置一个参数
        conf.set("param1", "this is param1");

        //也可以设置hadoop中xxxx-site.xml等等配置文件中的参数
        //conf.setBoolean("", "");
        //conf.set("", "");
        //2. 获取job对象, 设置job运行的主类
        Job job = Job.getInstance(conf, "param-demo");
        job.setJarByClass(ParamDemo.class);
        //3. 对map阶段设置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(conf.get("mapreduce.input.dir"))); //从配置文件中获取
        //4. 对reduce阶段设置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileOutputFormat.setOutputPath(job, new Path(conf.get("mapreduce.output.dir")));
        //5. 提交job
        int isok = job.waitForCompletion(true) ? 0 : 1;
        //退出job, 并打印信息
        System.exit(isok);
    }

    public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        public static Text k = new Text(); //定义输出的对象
        public static IntWritable v = new IntWritable(); //定义输出的值
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //1. 获取从输入文件的数据的每一行的值
            String line = value.toString();
            //2. 对每一行的数据进行切分(这里采用空格切分)
            String[] words = line.split(" ");
            //3. 循环处理(统计单词)
            for (String word : words){
                k.set(word); v.set(1);
                //map阶段的输出
                context.write(k, v);
            }
            //获取参数context.getConfiguration().get("param1")
            context.getCounter("参数1", context.getConfiguration().get("param1"));
        }
    }

    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            //自定义计数器
            int counter = 0;
            for (IntWritable i : values){
                counter += i.get();
            }
            //reduce输出:
            context.write(key, new IntWritable(counter));
        }
    }

}
