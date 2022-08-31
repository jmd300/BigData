-- dynamic partition insert 动态分区插入
-- 分区的值是由后续的select查询语句的结果动态确定的
-- 根据查询结果自动分区
-- hive.exec.dynamic,partition true 需要设置true为启用动态分区插入
-- hive.exec.dynamic.partition.mode strict 在strict模式下，用户必须至少指定一个静态分区，以防用户意外覆盖所有分区；在nonstrict模式下，允许所有分区都是动态的

-- 分区字段是有顺序的，必须依次放在最后
FROM source_table
INSERT OVERWRITE TABLE target_table PARTITION(day='20220808', hour)
SELECT column_names;
-- hour 分区将由SELECT子句动态创建
-- 而day分区是手动指定的
-- 如果是nonstrict模式下，day分区也可以动态创建

INSERT OVERWRITE TABLE target_table PARTITION(day='20220808', hour)
SELECT column_names
FROM source_table;