package com.anayasmi.orderService.services.kafkaService;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
@Slf4j
@Service
public class KafkaFileProcessService {
    private static final String TOPIC = "order-json-upload";
    public void sendJsonFileToKafka(String filePath) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            File file = new File(filePath);
            String jsonContent = new String(Files.readAllBytes(file.toPath()));

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, file.getName(), jsonContent);
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    log.info("JSON file sent to Kafka: {}", file.getName());
                } else {
                    log.error("Error sending JSON file to Kafka", exception);
                }
            });
        } catch (IOException e) {
            log.error("Failed to read or send file", e);
        }
    }
}
