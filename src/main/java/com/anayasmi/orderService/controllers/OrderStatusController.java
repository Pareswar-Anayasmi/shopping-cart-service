package com.anayasmi.orderService.controllers;

import com.anayasmi.orderService.models.request.OrderStatusRequest;
import com.anayasmi.orderService.models.response.OrderStatusResponse;
import com.anayasmi.orderService.services.kafkaService.OrderStatusProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/v1" )
@Slf4j
@RequiredArgsConstructor
public class OrderStatusController {

    private final OrderStatusProducerService orderStatusProducerService;

    @PostMapping("/orderStatus")
    public ResponseEntity<OrderStatusResponse> orderStatus(@RequestBody OrderStatusRequest orderStatusRequest) {
        try {
            orderStatusProducerService.sendMessage(orderStatusRequest.toString());
            return new ResponseEntity<OrderStatusResponse>(new OrderStatusResponse("Success"), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<OrderStatusResponse>(new OrderStatusResponse("Failed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
