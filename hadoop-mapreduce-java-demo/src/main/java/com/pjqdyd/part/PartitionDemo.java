package com.pjqdyd.part;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**   
 * @Description:  [分区操作]
 * @Author:       pjqdyd
 * 输入数据:
 * Hello pjqdyd hello
 * hello world Hi 1234
 * 结果将会在分区类中进行分区操作
 * (首字母A-Z的放一个文件, a-z放一个, 其它的放一个)
 * 输出数据:
 * 结果文件: part-r-00000
 * Hello 1
 * Hi 1
 *
 * 结果文件: part-r-00001
 * hello 2
 * world 1
 * pjqdyd 1
 *
 * 结果文件: part-r-00002
 * 1234 1
 * @Version:      [v1.0.0]
 */

public class PartitionDemo {


    //启动主方法
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取配置对象, 配置conf
        Configuration conf = new Configuration();
        //2. 获取job对象, 设置job运行的主类
        Job job = Job.getInstance(conf, "my-partition");
        job.setJarByClass(PartitionDemo.class);
        //3. 对map阶段设置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0])); //从控制台接收第一个参数

        //添加分区信息
        job.setPartitionerClass(MyPartitioner.class);
        job.setNumReduceTasks(3); //分3个文件(分区数numPartitions)

        //4. 对reduce阶段设置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //5. 提交job
        int isok = job.waitForCompletion(true) ? 0 : 1;
        //退出job, 并打印信息
        System.exit(isok);
    }


    //map阶段:
    public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //1. 获取从输入文件的数据的每一行的值
            String line = value.toString();
            //2. 对每一行的数据进行切分(这里采用空格切分)
            String[] words = line.split(" ");
            //3. 循环处理(统计单词)
            for (String word : words){
                //map阶段的输出
                context.write(new Text(word), new Text(String.valueOf(1)));
            }
        }
    }

    //reduce阶段:
    public static class MyReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //自定义计数器
            int counter = 0;
            for (Text t : values){
                counter += Integer.parseInt(t.toString());
            }
            //reduce输出:
            context.write(key, new Text(String.valueOf(counter)));
        }
    }


}
