-- 多重插入
FROM source_table
INSERT INTO target_table1
    SELECT column_names
INSERT INTO target_table2
    SELECT column_names