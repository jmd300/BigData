-- 目录可以是完整的url。如果未指定schema，则hive将使用hadoop 配置的fs.default.name来决定导出位置
-- 如果使用LOCAL 关键字，则hive会将数据写入本地文件系统上的目录
-- 如果写入文件系统的数据被序列化为文本，列之间用\001隔开，行之间用换行符隔开。如果列都不是原始数据类型，那么将这些列序列化为JSON格式。
-- 也可以在导出的时候指定分隔符换行符和文件格式
INSERT OVERWRITE DIRECTORY directory1
    [ROW FORMAT row_format][STORED AS file_dormat]
SELECT column_names
FROM source_table;

INSERT OVERWRITE DIRECTORY  'dir'
SELECT * FROM source_table;

INSERT OVERWRITE [LOCAL] DIRECTORY 'dir' ROW FORMAT DELIMATED FILEDS TERMINATED BY ',' STORED AS ORC
SELECT * FROM source_table;
