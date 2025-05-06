package com.anayasmi.orderService.services.kafkaService;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
@Slf4j
@Service
public class KafkaFileProcessService {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public void sendJsonFileToKafka(String filePath) {
       String TOPIC = "order-json-upload";

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

    public void sendPdfFileToKafka(String filePath) {

        String TOPIC = "order-pdf-topic";
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            File file = new File(filePath);

            if (!file.exists() || !file.isFile()) {
               log.error("❌ Invalid file path: " + filePath);
                return;
            }

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String base64Content = Base64.getEncoder().encodeToString(fileBytes);
            String fileName = file.getName();
            String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1) : "pdf";
            long size = file.length();

            Map<String, Object> fileData = new HashMap<>();
            fileData.put("fileName", fileName);
            fileData.put("extension", extension);
            fileData.put("size", size);
            fileData.put("content", base64Content);

            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(fileData);

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, fileName, jsonPayload);
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    log.info("✅ PDF received to Kafka successfully: " + fileName);
                } else {
                    log.error("❌ Failed to send PDF to Kafka: " + exception.getMessage());
                    exception.printStackTrace();
                }
            });

        } catch (IOException e) {
            log.error("❌ Error reading or sending the PDF file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
