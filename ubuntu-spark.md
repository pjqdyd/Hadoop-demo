#### Ubuntu 下安装spark-3.0.0-hadoop3.2 (单机主从)

1. 官网下载: https://archive.apache.org/dist/spark/spark-3.0.0-preview/
    
    安装包名: (spark-3.0.0-preview-bin-hadoop3.2.tar)
    
2. 将安装包移动到/usr/local目录下解压:
    `tar -zvxf spark-3.0.0-preview-bin-hadoop3.2.tar`

   进入目录:
    `cd spark-3.0.0-preview-bin-hadoop3.2/`
  
   进入conf目录: `cd conf/`
   
   重命名文件: `mv spark-env.sh.template spark-env.sh`
   
   编辑文件: `vi spark-env.sh`

 ```
    export JAVE_HOME=/usr/local/jdk1.8.0_201
    export SPARK_MASTER_IP=localhost
    export HADOOP_HOME=/usr/local/hadoop-3.2.1
    export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
 ```

   保存后进入spark安装目录下sbin/目录:
   运行 `./start-all.sh`启动spark
   
   运行`jps`命令即可查看启动的Worker和Master进程
   
   浏览器访问http://localhost:8080访问Spark web ui界面