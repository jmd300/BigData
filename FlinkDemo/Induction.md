1. Flink和Spark Streaming有什么区别?
Flink 是实时处理引擎，基于事件驱动。而 Spark Streaming 是微批（Micro-Batch）的模型。（根本区别，一定要说出来）
时间机制:Spark Streaming只支持处理时间。 Flink支持处理时间、事件时间、注入时间。同时也支持watermark来处理滞后数据。
容错机制:Spark Streaming 通过checkpoint实现数据不丢失，但无法做到恰好一次处理语义。Flink 则使用两阶段提交协议和checkpoint实现精准一次处理，容错性好
