#### Ubuntu下安装单机Hadoop

#### 前提: 
   1.安装jdk, 并配置好环境变量  (注意: jdk版本一定要是8)
   
   2.配置ssh免密登录:
   ```
   修改/etc/ssh/sshd_config:

    UsePrivilegeSeparation no
    PermitRootLogin no         #如果你确实要用root方式登录的话设为yes
    PasswordAuthentication yes
   ```
   重启ssh: `sudo service ssh --full-restart`
   
   执行`ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa`生成密钥
   
   执行`ssh-copy-id -i ~/.ssh/id_rsa.pub root@localhost`将公钥发送至本地Ubuntu
   
   运行ssh localhost查看是否不需要密码登录

   3.cd到/usr/local/下下载hadoop:
   ```
    sudo wget  wgt https://mirrors.cnnic.cn/apache/hadoop/common/hadoop-3.2.1/hadoop-3.2.1.tar.gz
   ```

   解压:
   `tar xzvf hadoop-3.2.1.tar.gz`
   
   4.设置HADOOP_HOME路径:
   `vim ~/.bashrc`
   
   ```
    export HADOOP_HOME=/usr/local/hadoop-3.2.1   
    export PATH=$PATH:$HADOOP_HOME/bin
   ```
   
   5.使生效: `source ~/.bashrc`
   
   6.修改/usr/local/hadoop-3.2.1/etc/hadoop/hadoop-env.sh文件中的JAVA_HOME变量:
   ```
    export JAVA_HOME=/usr/local/jdk1.8.0_201
   ```

  7.修改/usr/local/hadoop-3.2.1/etc/hadoop/core-site.xml:
   ```
    <configuration>
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://localhost:9000</value>
        </property>
    </configuration>
   ```

   8.修改/usr/local/hadoop-3.2.1/etc/hadoop/hdfs-site.xml:
   ```
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>1</value>
        </property>
         <property>
                <name>dfs.webhdfs.enabled</name>
                <value>true</value>
         </property>
    </configuration>
   ```
  
  9.配置YARN, 修改/usr/local/hadoop-3.2.1/etc/hadoop/mapred-site.xml:
  ```
    <configuration>
        <property>
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>
    </configuration>
  ```

  10.修改/usr/local/hadoop-3.2.1/etc/hadoop/yarn-site.xml如下：
  ```
    <configuration>
            <property>
                    <name>yarn.nodemanager.aux-services</name>
                    <value>mapreduce_shuffle</value>
            </property>
    
            <property>
                    <name>yarn.nodemanager.resource.memory-mb</name>
                    <value>20480</value>                                                                                                                                                          </property>
            </property>
    </configuration>
  ```
   修改usr/local/hadoop-3.2.1/etc/hadoop/mapred-site.xml如下:
    
   ```xml
    <configuration>
       <property>
               <name>mapreduce.framework.name</name>
               <value>yarn</value>
       </property>
       <property>
               <name>yarn.app.mapreduce.am.env</name>
               <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
       </property>
       <property>
            <name>mapreduce.map.env</name>
            <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
       </property>
       <property>
               <name>mapreduce.reduce.env</name>
               <value>HADOOP_MAPRED_HOME=${HADOOP_HOME}</value>
       </property>
    </configuration>
   ```

  11.格式化文件系统:`hdfs namenode -format`
  
  12.开启 NameNode 和 DataNode 守护进程:
  ```
    cd /usr/local/hadoop-3.2.1/sbin
    
    将start-dfs.sh，stop-dfs.sh两个文件顶部添加以下参数:
        HDFS_DATANODE_USER=root
        HADOOP_SECURE_DN_USER=hdfs
        HDFS_NAMENODE_USER=root
        HDFS_SECONDARYNAMENODE_USER=root
    还有start-yarn.sh，stop-yarn.sh顶部也需添加以下：
        YARN_RESOURCEMANAGER_USER=root
        HADOOP_SECURE_DN_USER=yarn
        YARN_NODEMANAGER_USER=root

    最后执行./start-dfs.sh启动hdfs
    执行./start-yarn.sh启动yarn
  ```
  13.运行`jsp`可以查看启动的进程
     访问http://localhost:9870可以进入web控制台
     
  14. 上传一个文件到hdfs, 用于测试:
      ```
      touch test.txt
      echo "Hello World" >> test.txt
      hadoop fs -mkdir -p input
      hdfs dfs -put ./test.txt input
      ```
      查看文件`hdfs dfs -lsr`