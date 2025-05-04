package com.anayasmi.orderService.helpar;

import com.anayasmi.orderService.models.response.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Slf4j
public class OrderJSONGenerator {

    private void saveOrderResponseToJSON(OrderResponse order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 1. Create formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");

            // 2. Create JavaTimeModule and add serializer with formatter
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));

            // 3. Register the module
            objectMapper.registerModule(javaTimeModule);

            // 4. Disable timestamps
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // 5. Save file
            String filename = "orders/order_" + order.getOrderCustomId() + "_" + System.currentTimeMillis() + ".json";
            File file = new File(filename);
            file.getParentFile().mkdirs(); // Ensure "orders/" folder is created
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, order);

            //this will send the file to kafka.
//            kafkaFileProcessService.sendJsonFileToKafka(filename);
            Path path = Paths.get(filename);
            try {
                // Delete the file if it exists
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace(); // You can use a logger here
            }

            log.info("OrderResponse saved to file: {}", filename);
        } catch (Exception e) {
            log.error("Failed to save OrderResponse to file", e);
        }
    }
}
