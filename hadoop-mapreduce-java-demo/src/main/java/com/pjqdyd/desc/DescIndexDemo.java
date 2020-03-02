package com.pjqdyd.desc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**   
 * @Description:  [倒排索引案例]
 * 输入数据(两个文件):
 * 1.html:
 * hadoop hadoop is good
 *
 * 2.html:
 * hadoop hbase is nice
 * 输出:
 * hadoop 1.html:2; 2.html:1
 * is 1.html:1; 2.html:1
 * good 1.html:1
 * hbase 2.html:1
 * nice 2.html:1
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class DescIndexDemo {


    //启动主方法
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取配置对象, 配置conf
        Configuration conf = new Configuration();
        //2. 获取job对象, 设置job运行的主类
        Job job = Job.getInstance(conf, "desc-index");
        job.setJarByClass(DescIndexDemo.class);
        //3. 对map阶段设置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0])); //从控制台接收第一个参数 1.html文件
        FileInputFormat.addInputPath(job, new Path(args[1])); //从控制台接收第二个参数 2.html文件

        //设置Combiner
        job.setCombinerClass(MyCombiner.class);

        //4. 对reduce阶段设置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        //5. 提交job
        int isok = job.waitForCompletion(true) ? 0 : 1;
        //退出job, 并打印信息
        System.exit(isok);
    }


    //map阶段
    public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //获取文件名字
            InputSplit is = context.getInputSplit();
            String fileName = ((FileSplit) is).getPath().getName();

            String line = value.toString();
            String[] words = line.split(" ");
            for (String word: words) {
                context.write(new Text(word + "_" + fileName), new Text(String.valueOf(1)));
            }
            //这里的输出将会是
            //hadoop_1.html 1
            //hadoop_1.html 1
            //is_1.html 1
            //hadoop_2.html 1
            //...
        }
    }


    //reduce阶段:
    public static class MyReducer extends Reducer<Text, Text, Text, Text> {
        //reduce输入的是(由Combiner输出):
        //hadoop 1.html:2
        //is 1.html:1
        //hadoop 2.html:1
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            String str = "";
            for (Text value : values){
                str += value.toString() + ";";
            }
            context.write(key, new Text(str));
            //最终的输出就是:
            //hadoop 1.html:2;2.html:1
            //is 1.html:1;2.html:1
            //...

        }
    }

}
