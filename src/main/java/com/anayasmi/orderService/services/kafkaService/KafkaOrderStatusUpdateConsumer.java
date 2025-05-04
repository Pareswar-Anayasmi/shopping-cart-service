package com.anayasmi.orderService.services.kafkaService;

import com.anayasmi.orderService.models.request.OrderStatusUpdateRequest;
import com.anayasmi.orderService.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaOrderStatusUpdateConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private  OrderService orderService;
    @KafkaListener(topics = "order-status-update-topic",  groupId = "my-status-id")
    public void listen(String message) {
        try{
                log.info("✅UpdateStatus request received to API: " + message);
                OrderStatusUpdateRequest request = objectMapper.readValue(message, OrderStatusUpdateRequest.class);
                orderService.approveRejectOrderThrowKafka(request);
            } catch (Exception e) {
                e.printStackTrace();
    }
    }
}
