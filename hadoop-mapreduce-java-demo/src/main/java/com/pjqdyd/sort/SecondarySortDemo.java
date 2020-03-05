package com.pjqdyd.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**   
 * @Description:  [二次排序案例]
 * 有两列数: (按照第一列排序, 如果第一列相同的, 再按第二列排)
 * 23 45
 * 34 54
 * 9 16
 * 78 19
 * 9 3
 * 输出后的排序:
 * 9 3
 * 9 16
 * 23 19
 * 34 45
 * 78 54
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class SecondarySortDemo {

    //启动主方法
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取配置对象, 配置conf
        Configuration conf = new Configuration();
        //2. 获取job对象, 设置job运行的主类
        Job job = Job.getInstance(conf, "secondary-sort");
        job.setJarByClass(SecondarySortDemo.class);
        //3. 对map阶段设置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(SecondarySortWritable.class);
        job.setMapOutputValueClass(NullWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0])); //从控制台接收第一个参数
        //4. 对reduce阶段设置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(SecondarySortWritable.class);
        job.setOutputValueClass(NullWritable.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //5. 提交job
        int isok = job.waitForCompletion(true) ? 0 : 1;
        //退出job, 并打印信息
        System.exit(isok);
    }


    public static class MyMapper extends Mapper<LongWritable, Text, SecondarySortWritable, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(" ");
            SecondarySortWritable sw = new SecondarySortWritable(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
            context.write(sw, NullWritable.get()); //这里的key就是经过排序的, 所以第二个value不需要
        }
    }

    public static class MyReducer extends Reducer<SecondarySortWritable, NullWritable, SecondarySortWritable, NullWritable> {
        @Override
        protected void reduce(SecondarySortWritable key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get()); //直接打印key就是排好序的
        }
    }

}
