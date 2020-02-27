package com.pjqdyd.part;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**   
 * @Description:  [自定义的分区信息类]
 * @Author:       pjqdyd
 * @Version:      [v1.0.0]
 */

public class MyPartitioner extends Partitioner<Text, Text> {

    @Override
    public int getPartition(Text key, Text value, int numPartitions) {
        String firstChar = key.toString().substring(0, 1);
        if (firstChar.matches("^[A-Z]")){
            return 0 % numPartitions; //取模保证返回数字是小于分区数numPartitions
        }else if (firstChar.matches("^[a-z]")){
            return 1 % numPartitions;
        }else {
         return 2 % numPartitions; //这里得到的输出文件就是part-r-00002
        }
    }
}
