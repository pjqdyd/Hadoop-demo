package com.pjqdyd.top;

import com.pjqdyd.writable.MyWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.TreeSet;

/**   
 * @Description:  [Top N 案例]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class TopNDemo {

    //启动主方法
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1. 获取配置对象, 配置conf
        Configuration conf = new Configuration();
        //2. 获取job对象, 设置job运行的主类
        Job job = Job.getInstance(conf, "top-n-demo");
        job.setJarByClass(TopNDemo.class);
        //3. 对map阶段设置
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0])); //从控制台接收第一个参数
        //4. 对reduce阶段设置
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(MyWritable.class);
        job.setOutputValueClass(NullWritable.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //5. 提交job
        int isok = job.waitForCompletion(true) ? 0 : 1;
        //退出job, 并打印信息
        System.exit(isok);
    }


    public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(" ");
            for (String word : words){
                context.write(new Text(word), new Text(String.valueOf(1)));
            }
        }
    }


    public static class MyReducer extends Reducer<Text, Text, MyWritable, NullWritable> {

        public static final int TOP_N = 3;
        //定义一个集合存储最终输出的结果
        TreeSet<MyWritable> treeSet = new TreeSet<MyWritable>();


        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int counter = 0;
            for (Text v : values){
                counter += Integer.parseInt(v.toString());
            }
            //构造最终的输出类型 ()
            MyWritable myWritable = new MyWritable(key.toString(), counter);
            //将每次输出添加到TreeSet集合
            treeSet.add(myWritable);

            //如果treeSet里面的数据个数大于TOP_N的时候, 移除多余的输出
            //(因为MyWritable已经实现的倒序排列, 所以这里是单词出现最多的前3个)
            if (treeSet.size() > TOP_N){
                treeSet.remove(treeSet.last());
            }

            //context.write(myWritable, NullWritable.get()); //取消,这里还是会每次输出
        }

        //构造最终输出
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
           //循环打印treeSet中的东西, (打印最大的3个)
            for (MyWritable myWritable : treeSet){
                context.write(myWritable, NullWritable.get());
            }
        }
    }

}
