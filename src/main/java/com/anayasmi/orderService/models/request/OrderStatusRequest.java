package com.anayasmi.orderService.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {

    private String orderNumber;
    private String status;

    @Override
    public String toString() {
        return "{"
                + "\"orderNumber\":\"" + orderNumber + "\","
                + "\"status\":\"" + status + "\""
                + "}";

    }
}
