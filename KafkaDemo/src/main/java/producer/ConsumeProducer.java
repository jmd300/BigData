package producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ConsumeProducer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 配置
        Properties properties = new Properties();

        // 连接集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");

        // 指定对应的key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 创建Kafka生产者对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        for (int i = 0; i < 5; i++) {
            // 同步发送
            kafkaProducer.send(new ProducerRecord<>("first", "zookper-kafka" + i)).get();

            // 异步发送
            // kafkaProducer.send(new ProducerRecord<>("first", "zookper-kafka" + i));

            kafkaProducer.send(new ProducerRecord<>("first", "zookper-kafka" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(e == null){
                        System.out.println("主题" + recordMetadata.topic());
                        System.out.println("分区" + recordMetadata.partition());
                    }
                }
            });
        }

        
        kafkaProducer.close();
    }
}
