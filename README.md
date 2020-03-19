### Java 整合使用 Hadoop Hdfs Hive MapReduce Spark 的案例项目

#### 项目依赖:
  JDK 8
  
  Hadoop 3.2.1
  
  Hive 3.1.2
  
  Mysql 5.7
  
  Spark 3.0.0-preview
  
  Scala 2.12.10

#### 项目结构:
 ```
  ├─hadoop-hdfs-java-demo           Java访问hadoop hdfs的模块
  ├─hadoop-mapreduce-java-demo      Java编写hadoop mapreduce的模块
  ├─hadoop-hive-java-demo           Java操作hadoop hive的模块
  ├─hadoop-spark-scala-demo         Scala编写hadoop spark程序的模块
  ├─out                             单独打包jar包存放的目录
  ├─docker-hadoop                   Docker中安装Hadoop集群文件目录
  ├─docker-hadoop.md                Docker中安装Hadoop集群说明
  ├─ubuntu-hadoop.md                Ubuntu下安装Hadoop说明 (推荐)
  ├─ubuntu-hive.md                  Ubuntu下安装Hive说明 (推荐)
  ├─ubuntu-spark.md                 Ubuntu下安装Spark说明 (推荐)
  ├─.gitignore                      .gitignore文件
  ├─README.md                       README.md文件
  └─pom.xml                         父pom文件
 ```
