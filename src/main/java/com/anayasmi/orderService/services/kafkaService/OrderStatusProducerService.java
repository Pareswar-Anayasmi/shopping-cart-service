package com.anayasmi.orderService.services.kafkaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderStatusProducerService {
    private static final String TOPIC = "order-status-topic";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        System.out.println("Producing message: " + message);
        kafkaTemplate.send(TOPIC, message);
    }



}

