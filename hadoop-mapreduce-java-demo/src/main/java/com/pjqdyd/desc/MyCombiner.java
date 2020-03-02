package com.pjqdyd.desc;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**   
 * @Description:  [自定义Combiner]
 * 1. 需要继承Reducer, Combiner是reducer的提前执行, 特殊实现
 * 2. Combiner的map结果输入, 对reducer输出类型要一致, 不影响最终结果类型
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class MyCombiner extends Reducer<Text, Text, Text, Text> {

    //这里的输入是:
    //hadoop_1.html 1
    //hadoop_1.html 1
    //is_1.html 1
    //hadoop_2.html 1
    //...
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
       String[] str = key.toString().split("_");
       int counter = 0;
       for(Text v : values){
           counter += Integer.parseInt(v.toString());
       }
       context.write(new Text(str[0]), new Text(str[1] + ":" + counter));
        //输出的是:
        //hadoop 1.html:2
        //is 1.html:1
        //hadoop 2.html:1
    }
}
