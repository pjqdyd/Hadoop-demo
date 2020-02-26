package com.pjqdyd.math;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**   
 * @Description:  [将某生产线三个时间段的记录求平均值]
 * 生产线L     Z早      晚W      夜Y
 *    L1       857      345       45
 *    L2       458      454       454
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class AvgDemo {
    //启动主方法
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取配置对象, 配置conf
        Configuration conf = new Configuration();
        //2. 获取job对象, 设置job运行的主类
        Job job = Job.getInstance(conf, "avg-demo");
        job.setJarByClass(AvgDemo.class);
        //3. 对map阶段设置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FloatWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0])); //从控制台接收第一个参数
        //4. 对reduce阶段设置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //5. 提交job
        int isok = job.waitForCompletion(true) ? 0 : 1;
        //退出job, 并打印信息
        System.exit(isok);
    }



    //map阶段
    public static class MyMapper extends Mapper<LongWritable, Text, Text, FloatWritable> {
        public static Text k = new Text(); //定义输出的对象
        public static FloatWritable v = new FloatWritable(); //定义输出的值
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //1. 获取从输入文件的数据的每一行的值
            String line = value.toString();
            //2. 对每一行的数据进行切分(这里采用tab切分)
            String[] words = line.split("\t");
            //3. 按照业务处理
            String lineName = words[0]; //生产线名
            int z = Integer.parseInt(words[1]);
            int w = Integer.parseInt(words[2]);
            int y = Integer.parseInt(words[3]);
            float avg = (float) (z + w + y) / 3;
            k.set(lineName); v.set(avg);
            context.write(k, v);
        }

        //在map之前仅会执行一次的方法, 一般用于初始化
        //@Override
        //protected void setup(Context context) throws IOException, InterruptedException { }
        //在map之后仅会执行一次的方法,
        //@Override
        //protected void cleanup(Context context) throws IOException, InterruptedException { }
    }

    //reduce阶段:
    //这里输入的前两个类型是上方map的后两个输出类型, 这里的后两个类型是最终结果类型
    public static class MyReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
        //reduce输入的是:
        // L1 495.4
        // L2 947.34
        @Override
        protected void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, new FloatWritable(values.iterator().next().get()));
        }
    }

}
