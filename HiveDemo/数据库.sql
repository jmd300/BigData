---创建数据库
create (DATABASE|SCHEMA) [IF NOT EXISTS] database_name
  [comment database_comment]
  [LOCATION hdfs_path]
  [with DBPROPERTIES (property_name=property_value, ...)];

create database if not exists school
    comment '小学表'
    location 'hdfs://hive/database/student/'
    with dbproperties ('createtime' = '20220808', 'creator' = 'xiaoming');

--- hive的表存放位置模式是由hive-site.xml当中的一个属性指定的
--- <name>hive.metastore.warehouse.dir</name>
--- <value>/user/hive/warehouse</value>

--- 修改数据库的属性
alter  database  school  set  dbproperties('createtime'='20220818');

--- 查看数据路基本信息
desc  database  school;

--- 查看数据库详细信息
desc database extended  school;

--- 删除数据库
--- 删除一个空数据库，如果数据库下面有数据表，那么就会报错
drop  database  school;

--- 强制删除数据库，包含数据库下面的表一起删除
drop  database  school  cascade;

--- 创建数据库表语法
CREATE [EXTERNAL] TABLE [IF NOT EXISTS] table_name
   [(col_name data_type [COMMENT col_comment], ...)]
   [COMMENT table_comment]
   [PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]
   [CLUSTERED BY (col_name, col_name, ...)
   [SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS]
   [ROW FORMAT row_format]
   [STORED AS file_format]
   [LOCATION hdfs_path]

--- 说明：
--- 1. CREATE TABLE 创建一个指定名字的表。如果相同名字的表已经存在，则抛出异常；用户可以用 IF NOT EXISTS 选项来忽略这个异常。
--- 2. EXTERNAL关键字可以让用户创建一个外部表，在建表的同时指定一个指向实际数据的路径（LOCATION），Hive 创建内部表时，会将数据移动到数据仓库指向的路径.
--- 若创建外部表，仅记录数据所在的路径，不对数据的位置做任何改变。在删除表的时候，内部表的元数据和数据会被一起删除，而外部表只删除元数据，不删除数据。
--- 3. LIKE 允许用户复制现有的表结构，但是不复制数据。
--- 4. 用户在建表的时候可以自定义 SerDe 或者使用自带的 SerDe。如果没有指定 ROW FORMAT 或者 ROW FORMAT DELIMITED，将会使用自带的 SerDe。
--- 在建表的时候，用户还需要为表指定列，用户在指定表的列的同时也会指定自定义的 SerDe，Hive通过 SerDe 确定表的具体的列的数据。
--- 5. STORED AS SEQUENCEFILE|TEXTFILE|RCFILE
--- 如果文件数据是纯文本，可以使用 STORED AS TEXTFILE。如果数据需要压缩，使用 STORED AS SEQUENCEFILE。
--- 6、CLUSTERED BY
--- 对于每一个表（table）或者分区， Hive可以进一步组织成桶，也就是说桶是更为细粒度的数据范围划分。Hive也是 针对某一列进行桶的组织。Hive采用对列值哈希，然后除以桶的个数求余的方式决定该条记录存放在哪个桶当中。
--- 把表（或者分区）组织成桶（Bucket）有两个理由：
--- （1）获得更高的查询处理效率。桶为表加上了额外的结构，Hive 在处理有些查询时能利用这个结构。具体而言，连接两个在（包含连接列的）相同列上划分了桶的表，可以使用 Map 端连接 （Map-side join）高效的实现。比如JOIN操作。对于JOIN操作两个表有一个相同的列，如果对这两个表都进行了桶操作。那么将保存相同列值的桶进行JOIN操作就可以，可以大大较少JOIN的数据量。
--- （2）使取样（sampling）更高效。在处理大规模数据集时，在开发和修改查询的阶段，如果能在数据集的一小部分数据上试运行查询，会带来很多方便。

--- 分区相关操作
--- 添加分区
alter table score add partition(month='201804') partition(month = '201803');
--- 删除分区数据
alter table score drop partition(month = '201806');

--- 分桶表相关操作
--- 将数据按照指定的字段进行分成多个桶中去，就是将数据按照字段进行划分，可以将数据按照字段划分到多个文件当中去

--- 开启hive的桶表功能
set hive.enforce.bucketing=true;

--- 设置reduce的个数
set mapreduce.job.reduces=3;

--- 创建桶表
create table course (c_id string,c_name string,t_id string) clustered by(c_id) into 3 buckets row format delimited fields terminated by '\t';

--- 桶表的数据加载，只能通过insert overwrite。hdfs dfs -put文件或者通过load data无法加载
--- 创建普通表，并通过insert overwrite的方式将普通表的数据通过查询的方式加载到桶表当中去
--- 创建普通表：
create table course_common (c_id string,c_name string,t_id string) row format delimited fields terminated by '\t';

load data local inpath '/export/servers/hivedatas/course.csv' into table course_common;

--- 通过insert overwrite给桶表中加载数据
insert overwrite table course select * from course_common cluster by(c_id);

--- 修改表
--- 表重命名
alter table score4 rename to score5;

--- 查询表结构
desc table_name;

--- 添加列
alter table score5 add columns (mycol string, mysco string);

--- 更新列
alter table score5 change column mysco mysconew int;

--- 直接向分区表中插入数据
insert into table score3 partition(month ='201807') values ('001','002','100');

--- 通过查询方式加载数据
insert overwrite table score4 partition(month = '201806') select s_id,c_id,s_score from score;

--- hive表中的数据导出（了解）
--- 将hive表中的数据导出到其他任意目录，例如linux本地磁盘，例如hdfs，例如mysql等等
--- insert导出
--- 将查询的结果导出到本地
insert overwrite local directory '/export/servers/exporthive/a' select * from score;

--- 将查询的结果格式化导出到本地
--- collection items terminated by ‘#’ 对集合类型使用#来进行分割
insert overwrite local directory '/export/servers/exporthive' row format delimited fields terminated by '\t' collection items terminated by '#' select * from student;

--- 将查询的结果导出到HDFS上(没有local)
insert overwrite directory '/export/servers/exporthive' row format delimited fields terminated by '\t' collection items terminated by '#' select * from score;

--- export导出到HDFS上
export table score to '/export/exporthive/score';


--- 清空表数据,只能清空管理表，也就是内部表
truncate table score5;

--- as （直接使用查询结果插入到一张新表） as并没有把分隔符复制过来，只复制了查询的字段和字段对应的值
create table test222 as select * from student_pf where city=‘nanjing’;

--- like（复制表结构）
create table test333 like student_pf;
