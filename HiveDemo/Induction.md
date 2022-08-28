1. spark union和hive union的区别?
spark中data frame 有union和union all算子，均不去重
这点，不像hive中那样，hive sql中union all不去重，union去重

2. 创建表：
（1）简单创建示例：
```
create table student2(
num int,
name string
)
row format delimited fields terminated by'\t';
```

