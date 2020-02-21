#### 一, 在docker中安装单机hadoop

   1. 下载镜像`docker pull sequenceiq/hadoop-docker:latest`
   
   2. 启动容器:
   ```
    docker run -dit -p 50070:50070 -p 9000:9000 -p 8088:8088 -p 8040:8040 -p 8042:8042  -p 49707:49707  -p 50010:50010  -p 50075:50075  -p 50090:50090 sequenceiq/hadoop-docker:latest /etc/bootstrap.sh -bash   ```
   ```  
   3. 启动容器后, 访问http://docker主机ip:50070进入web控制台页面

<br/><br/>
#### 二, 在docker中安装hadoop集群

##### 参考的项目: [github仓库](https://github.com/big-data-europe/docker-hadoop)

##### 如何安装:

   1. 将本项目中的docker-hadoop文件夹上传到安装docker的主机上
     
   2. 在该目录下运行`docker-compose up -d`启动容器
   
   3. 等待镜像下载和容器启动成功后, 访问http://docker主机ip:9870即可进入web控制台
   
##### 注意:
   
   1. 如果在虚拟机中启动, 建议分配4G以上的内存空间保证docker中hadoop集群运行流畅
   
   2. docker-hadoop/hadoop.env文件中定义了一些hadoop集群组件的配置, 跟
      hadoop安装目录下/etc/hadoop/**.xml的配置是等效的
      
   3. 这个docker-compose会启动5个docker容器, 如果有容器掉了, 可能是分配的内存和cpu
      不满足hadoop运行条件所致, 可以修改hadoop.env增大相关内存, 或重新运行`docker-compose up -d`
      重启容器
      
   4. 运行`docker exec -it namenode /bin/bash`可交互式进入namenode容器