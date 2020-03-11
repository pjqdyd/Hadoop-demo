#### Ubuntu下安装Hive 3.1.2

1. 官网下载到压缩包: https://mirror.bit.edu.cn/apache/hive/hive-3.1.2/

2. `tar -zvxf apache-hive-3.1.2-bin.tar.gz` 解压文件, 并重命名目录为
    apache-hive-3.1.2
    
3. `vi ~/.bashrc` 配置环境变量
    ```
    export HIVE_HOME=/usr/local/apache-hive-3.1.2
    export PATH=$PATH:$HADOOP_HOME/bin:$HIVE_HOME/bin
    ```
   
4. `source ~/.bashrc`使变量生效

    (这里我们使用了mysql作为hive的metastore独立数据库，所以，在运行hive之前，
     请确保mysql数据库已经安装并且已启动, 建立一个数据库db_hive)

5. cd 到/usr/local/apache-hive-3.1.2/conf目录下:
    新建一个文件`vi hive-site.xml` 配置如下Mysql连接信息:
    ```
        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

        <configuration>
            <property>
                    <name>hive.defaulet.fileformat</name>
                    <value>TextFile</value>                                                                                                                   <value>TextFile</value>
            </property>
            <property>
                    <name>javax.jdo.option.ConnectionURL</name>
                    <value>jdbc:mysql://localhost:3306/db_hive</value>
            </property>
            <property>
                    <name>javax.jdo.option.ConnectionDriverName</name>                                                                                                                           <value>com.mysql.jdbc.Driver</value>
            </property>
            <property>
                    <name>javax.jdo.option.ConnectionUserName</name>
                    <value>root</value>
            </property>
            <property>
                    <name>javax.jdo.option.ConnectionPassword</name>
                    <value>123456</value>
            </property>
    </configuration>
    ```
   
   6. 在conf目录下`cp hive-env.sh.template hive-env.sh`并添加:
   ```
   export HADOOP_HOME=/usr/local/hadoop-3.2.1
   export HIVE_CONF_DIR=/usr/local/apache-hive-3.1.2/conf
   export HIVE_AUX_JARS_PATH=/usr/local/apache-hive-3.1.2/lib
   ```
   
   7. 拷贝一个mysql-connector-java-5.1.16-bin.jar包到hive的lib目录下.
   
   8. 保证Hadoop是启动的, 运行命令`hive`可以启动hive
   
   (提示如果出现java.lang.NoSuchMethodError: com.google.common.base.Preconditions.checkArgument
   则表示是因为
   
   这是因为hive内依赖的guava.jar和hadoop内的版本不一致造成的。 检验方法：
   
   查看hadoop安装目录下share/hadoop/common/lib内guava.jar版本
   查看hive安装目录下lib内guava.jar的版本 如果两者不一致，删除版本低的，并拷贝高版本的 问题解决)