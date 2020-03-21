#### Hadoop Spark 使用Scala语言编写的程序模块

##### 1. SparkWordCount程序如何运行:

1. 打包, pom文件中<mainClass>配置成SparkWordCount

2. 在本模块下的目录运行`mvn clean package`打包, 或
    在Idea的Maven右面板的Lifecycle下双击package打包
    
    出现BUILD SUCCESS表示打包成功, 在target目录下即可找到
    打出来的jar包: hadoop-spark-scala-demo-1.0-SNAPSHOT.jar
    
3. 将jar包发送到spark主机上(比如放在/tmp目录下):

   再在/tmp目录下创建一个test3.txt文件, 内容如下:
  ```
    Hello World
    Hello Hadoop
    Hello Spark
  ```
   确保Hadoop集群已启动:
   上传test3.txt文件到hdfs的/user/root/input目录下:
   `hdfs dfs -put ./test3.txt input`
    
   进入spark的安装目录下的/bin:
   提交运行程序 (注意反斜杠后回车输入下一行, 如果是spark集群要指定--master spark://MasterIP:7077 \): 
  ```
      ./spark-submit \
      > --executor-memory 1G \
      > --total-executor-cores 2 \
      > /tmp/hadoop-spark-scala-demo-1.0-SNAPSHOT.jar hdfs://localhost:9000/user/root/input/test3.txt hdfs://localhost:9000/user/root/output-spark
  ```
   等待运行成功后, 输入`hdfs dfs -cat /user/root/output-spark/part-00000`或part-00001
   可查看输出的结果