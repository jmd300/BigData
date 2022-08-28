### 1. 开启内置zookper和kafka 可使用 nohup command & 使程序后台持续运行
```nohup zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties &```

```nohup kafka-server-start.sh $KAFKA_HOME/config/server.properties &```

### 2. 创建topic
```bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic first```

```bin/kafka-topics.sh --create --bootstrap-server hadoop102:9092 --replication-factor 1 --partitions 1 --topic second```

### 3. 列举topic
```kafka-topics.sh --bootstrap-server hadoop102:9092 --list```

### 3. 查看topic详细信息
```kafka-topics.sh --bootstrap-server hadoop102:9092 --topic first --describe```

### 4. 修改partitions（只能增加不能减少，分区可能正在被访问，不好操作）不能通过命令行的方式修改副本的数量
```kafka-topics.sh --bootstrap-server hadoop102:9092 --topic first --alter --partitions 3```

### 5. kafka生产/发送消息
kafka-console-producer.sh --bootstrap-server hadoop102:9092 --topic first

### 6. kafka消费/接受消息
```kafka-console-consumer.sh --bootstrap-server hadoop102:9092 --topic first```

```kafka-console-consumer.sh --bootstrap-server hadoop102:9092 --topic first --from-beginning```

Kafka是scala和java开发的多分区、多副本且基于Zookeeper协调的分布式消息系统-分布式流处理平台。

1. kafka 分区的好处
便于合理使用存储资源：每个partition 在一个Broker上存储，可以把海量的数据按照分区切割成多块数据存储在多台Broker上。
合理控制分区的任务，可以实现负载均衡的效果。
提高并行度：生产者可以以分区为单位发送数据，消费者也可以以分区为单位消费数据。

2. kafka分区策略 
DefaultPartitioner，轮询策略 ：也称 Round-robin 策略，即顺序分配。比如一个 topic 下有 3 个分区，
那么第一条消息被发送到分区 0，第二条被发送到分区 1，第三条被发送到分区 2，以此类推。当生产第四条消息时又会重新开始。
轮询策略是 kafka java 生产者 API 默认提供的分区策略。轮询策略有非常优秀的负载均衡表现，它总是能保证消息最大限度地被平均分配到所有分区上，
故默认情况下它是最合理的分区策略，也是平时最常用的分区策略之一。

随机策略，也称 Randomness 策略。所谓随机就是我们随意地将消息放置在任意一个分区上

按照key分配策略，kafka 允许为每条消息定义消息键，简称为 key。一旦消息被定义了 key，那么你就可以保证同一个 key 的所有消息都进入到相同的分区里面

3. kafka为什么高吞吐量给？
追加：kafka的消息是不断追加到文件中的，这个特性使kafka可以充分利用磁盘的顺序读写性能。
顺序读写：顺序读写不需要硬盘磁头的寻道时间，

零拷贝：在Linux kernel2.2 之后出现了一种叫做"零拷贝(zero-copy)"系统调用机制，就是跳过“用户缓冲区”的拷贝，建立一个磁盘空间和内存的直接映射， 
数据不再复制到“用户态缓冲区”

分区：kafka 中的topic的内容可以被分为多个partition，每个partition又分为多个段segment，所以每次操作都是针对一小部分操作。

批量发送：kafka允许进行批量发送消息，producer发送消息的时候，可以将消息缓存在本地,等到了固定条件发送到kafka
等消息条数到固定条数，一段时间发送一次。

数据压缩：Kafka还支持对消息集合进行压缩，Producer可以通过GZIP或Snappy格式对消息集合进行压缩
压缩的好处就是减少传输的数据量，减轻对网络传输的压力

批量发送和数据压缩一起使用,单条做数据压缩的话，效果不明显

4. 默认消息保留策略
日志片段通过 log.segment.bytes 配置（默认是1GB）
日志片段通过 log.segment.ms 配置 （默认7天）

5. 消息确认(ack 应答)机制了解吗？
为保证 producer 发送的数据，能可靠的达到指定的 topic ,Producer 提供了消息确认机制。生产者往 Broker 的 topic 中发送消息时，
可以通过配置来决定有几个副本收到这条消息才算消息发送成功。可以在定义 Producer 时通过 acks 参数指定，这个参数支持以下三种值：

（1）acks = 0：producer 不会等待任何来自 broker 的响应。
特点：低延迟，高吞吐，数据可能会丢失。
如果当中出现问题，导致 broker 没有收到消息，那么 producer 无从得知，会造成消息丢失。

（2）acks = 1（默认值）：只要集群中 partition 的 Leader 节点收到消息，生产者就会收到一个来自服务器的成功响应。

如果在 follower 同步之前，leader 出现故障，将会丢失数据。

此时的吞吐量主要取决于使用的是 同步发送 还是 异步发送 ，吞吐量还受到发送中消息数量的限制，例如 producer 在收到 broker 响应之前可以发送多少个消息。

（3）acks = -1：只有当所有参与复制的节点全部都收到消息时，生产者才会收到一个来自服务器的成功响应。

这种模式是最安全的，可以保证不止一个服务器收到消息，就算有服务器发生崩溃，整个集群依然可以运行。

根据实际的应用场景，选择设置不同的 acks，以此保证数据的可靠性。
Producer 发送消息还可以选择同步或异步模式,如果设置成异步，虽然会极大的提高消息发送的性能，但是这样会增加丢失数据的风险。
如果需要确保消息的可靠性，必须将 producer.type 设置为 sync。

6. 什么是副本？

7. kafka分区与segment？
每个分区都是一个有序、不可变的消息序列，后续新来的消息会源源不断地、持续追加到分区的后面。

分区中的消息数据是存储在日志文件中的，而且同一分区中的消息数据是按照发送顺序严格有序的。
分区在逻辑上对应一个日志，当生产者将消息写入分区中时，实际上是写到了分区所对应的日志当中。
而日志可以看作是一种逻辑上的概念，它对应于磁盘上的一个目录。一个日志文件由多个Segment（段）来构成，每个Segment对应于一个索引文件与一个日志文件。

借助于分区，我们可以实现Kafka Server的水平扩展。对于一台机器来说，无论是物理机还是虚拟机，其运行能力总归是有上限的。
当一台机器到达其能力上限时就无法再扩展了，即垂直扩展能力总是受到硬件制约的。
通过使用分区， 可以将一个主题中的消息分散到不同的Kafka Server上（Kafka集群），
这样当机器的能力不足时，添加机器，创建新的分区，这样理论上就可以实现无限的水平扩展能力。

分区还可以实现并行处理能力，向一个主题所发送的消息会发送给该主题所拥有的不同的分区中，这样消息就可以实现并行发送与处理，由多个分区来接收所发送的消息。

一个分区（partition）是由一系列有序、不可变的消息所构成的。一个partition中的消息数量可能会非常多，因此显然不能将所有消息都保存到一个文件当中。
因此，类似于log4j的rolling log，当partition中的消息数量增长到一定程度之后，消息文件会进行切割，新的消息会被写到一个新的文件当中，
当新的文件增长到一定程度后，新的消息又会被写到另一个新的文件当中，以此类推；这一个个新的数据文件我们就称之为segment（段）。
因此，一个partition在物理上是由一个或者多个segment所构成的。每个segment中则保存了真实的消息数据。

partition与segment之间的关系:
每个partition都相当于一个大型文件被分配到多个大小相等的segment数据文件中，每个segment中的消息数量未必相等
（这与消息大小有着紧密的关系，不同的消息所占据的磁盘空间显然是不一样的），这个特点使得老的segment文件可以很容易就被删除掉，有助于提升磁盘的利用效率。

每个partition只需要支持顺序读写即可，segment文件的生命周期是由Kafka Server的配置参数所决定的。
比如说，server.properties文件中的参数项log.retention.hours=168就表示7天后删除老的消息文件。


关于分区目录中的4个文件的含义与作用：
00000000000000000000.index：它是segment文件的索引文件，它与接下来我们要介绍的00000000000000000000.log数据文件是成对出现的。
后缀.index就表示这是个索引文件。

00000000000000000000.log：它是segment文件的数据文件，用于存储实际的消息。该文件是二进制格式的。
segment文件的命名规则是partition全局的第一个segment从0开始，后续每个segment文件名为上一个segment文件最后一条消息的offset值。没有数字则用0填充。由于这里的主题的消息数量较少，因此只有一个数据文件。

00000000000000000000.timeindex：该文件是一个基于消息日期的索引文件，主要用途是在一些根据日期或是时间来寻找消息的场景下使用，
此外在基于时间的日志rolling或是基于时间的日志保留策略等情况下也会使用。实际上，该文件是在Kafka较新的版本中才增加的，老版本Kafka是没有该文件的。
它是对*.index文件的一个有益补充。*.index文件是基于偏移量的索引文件，而*.timeindex则是基于时间戳的索引文件。

leader-epoch-checkpoint：是leader的一个缓存文件。实际上，它是与Kafka的HW（High Watermark）与LEO（Log End Offset）相关的一个重要文件。

8. HW(High Watermark)
HW（High Watermark）：俗称高水位，它标识了一个特定的消息偏移量（offset），消费者只能拉取到这个 offset 之前的消息。
对消费者而言只能消费 HW 之前的消息。
   
9. LEO(LOG End Offset)
分区 ISR 集合中的每个副本都会维护自身的 LEO（Log End Offset）：俗称日志末端位移，而 ISR 集合中最小的 LEO 即为分区的 HW，

10. ISR(In-Sync Replicas)
kafka 为了保证数据的一致性使用了isr 机制
首先我们知道kafka 的数据是多副本的，某个topic的replication-factor为N且N大于1时，每个Partition都会有N个副本(Replica)。
kafka的replica包含leader与follower。每个topic 下的每个分区下都有一个leader 和(N-1)个follower，
每个follower 的数据都是同步leader的 这里需要注意 是follower 主动拉取leader 的数据。
Replica的个数小于等于Broker的个数，也就是说，对于每个Partition而言，每个Broker上最多只会有一个Replica，

follewer 只是数据的副本提供数据的可恢复性，本身和kafka 的读写性能无关（kafka的读写都是和leader 相关），
那么每个分区都有多个副本，这样该如何确定副本的数据和leader 的数据是同步的？

isr 的全称是：In-Sync Replicas isr 是一个副本的列表，里面存储的都是能跟leader 数据一致的副本，确定一个副本在isr列表中，有2个判断条件：
（1）根据副本和leader 的交互时间差，如果大于某个时间差 就认定这个副本不行了，就把此副本从isr 中剔除，此时间差根据
配置参数rerplica.lag.time.max.ms=10000 也就是默认10s，isr中的follow没有向isr发送心跳包就会被移除

（2）根据leader 和副本的信息条数差值决定是否从isr 中剔除此副本，此信息条数差值根据配置参数
rerplica.lag.max.messages=4000 决定 ，也就是默认消息差大于4000会被移除
kafka后续版本移除了第二个判断条件，只保留了第一个，以内极端情况下，如果producor一次性发来了10000条数据，而默认条数差立马会大于4000




























