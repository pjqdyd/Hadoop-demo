#### Hive创建表, 导入数据相关操作

```
create table if not exists tb_student(
    id int,
    name string
) 
comment '学生表' 
row format delimited fields terminated by '\t' 
lines terminated by '\n'
stored as textfile;
```

上面语句表示, 用\t也就是Tab切分字段, \n切分行,
数据格式是默认的TextFile
```
准备一份数据 /tmp/student.txt:
1001    小明
1002    小红

在hive下导入加载本地数据, 进入/tmp目录下运行hive:
use db_my;
load data local inpath 'student.txt' into table tb_student;

(加载hdfs文件系统下的文件也行, inpath写hdfs下的路径)

(该表也会生成在hdfs下的/user/hive/warehouse/db_my.db/tb_student文件中)
```   

##### Hive的常用记录格式 (serde): csv, tsv, json, RegEx正则, parqut
(serde序列化与反序列化: ser: Serializer   de: Deserializer)

创建记录为csv格式的表:
```
create table if not exists tb_csv(
    name string
    score string
) 
row format serde 'org.apache.hadoop.hive.serde2.OpenCSVSerde' 
stored as textfile;
```
加载数据:
`load data local inpath '/tmp/test.csv' into table tb_csv;`

(注意默认csv是字段以","为分隔符, 也可以单独指定):
```
row format serde 'org.apache.hadoop.hive.serde2.OpenCSVSerde' 
with serdeproperties(
    "separatorChar"=",",
    "quoteChar"="'",
    "esacpeChar"="\\"
) 
stored as textfile;
```

(hive还支持如使用`hive --database db_my -f xxx.sql`的方式创建表)