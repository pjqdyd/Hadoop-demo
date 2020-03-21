package com.pjqdyd.word

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

//Spark的单词统计程序
object SparkWordCount {
  //Main方法
  def main(args: Array[String]): Unit = {

    //配置Spark
    val conf = new SparkConf().setAppName("SparkWordCount")
    //创建SparkContext对象
    val sc = new SparkContext(conf)

    //读取文件, 接收控制台的输入参数1, 即输入文件路径
    val lines: RDD[String] = sc.textFile(args(0))
    //按空格切分单词
    val words: RDD[String] = lines.flatMap(_.split(" "))

    //将单词组成元组
    val tuples: RDD[(String, Int)] = words.map((_, 1))

    //spark中所提供的一个算子,这个算子reduceByKey可以将相同的key的value进行求和
    val sumed: RDD[(String, Int)] = tuples.reduceByKey(_ + _)

    //对结果进行排序,_1表示第一个key值(String), _2表示第二个value值(Int)
    //默认true表示倒序排序
    val sorted: RDD[(String, Int)] = sumed.sortBy(_._2, false)

    //将结果提交到spark集群中存储,(接收控制台的输入参数2, 即输出文件路径)
    sorted.saveAsTextFile(args(1))

    //关闭上下文
    sc.stop()
  }
}
