package com.pjqdyd.mapreduce;

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
 * @Description:  [自定义单词计算程序, 打包成可执行my-count.jar包在hadoop上运行]
 * (yarn jar ./my-count.jar /user/root/input/test2.txt /user/root/output/test2)
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class MyWordCount {

    //启动主方法
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取配置对象, 配置conf
        Configuration conf = new Configuration();
        //2. 获取job对象, 设置job运行的主类
        Job job = Job.getInstance(conf, "mywordcount");
        job.setJarByClass(MyWordCount.class);
        //3. 对map阶段设置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0])); //从控制台接收第一个参数
        //4. 对reduce阶段设置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //5. 提交job
        int isok = job.waitForCompletion(true) ? 0 : 1;
        //退出job, 并打印信息
        System.exit(isok);
    }


    //input阶段:

    //map阶段: (行偏移量,每一行的第一个字母距离该文件首位置的距离)
    //比如有文本:
    /**
     *hello world
     *hello mapreduce
     */
    //那么map阶段的输入就是(每一行输入就执行一次方法)
    //0 hello world
    //11 hello mapreduce
    //输入key的类型KEYIN, 输入行的值类型VALUEIN, 输出key的类型KEYOUT, 输出值的类型VALUEOUT,
    public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
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
        }
    }
    //map阶段的输出是:
    //hello 1
    //hello 1
    //world 1
    //mapreduce 1


    //reduce阶段:
    //这里输入的前两个类型是上方map的后两个输出类型, 这里的后两个类型是最终结果类型
    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
        //reduce输入的是:
        /**
         *hello list[1, 1]
         *world list[1 ]
         *mapreduce list[1 ]
         */
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
    //reduce阶段的输出是:
    //hello 2
    //world 1
    //mapreduce 1
}
