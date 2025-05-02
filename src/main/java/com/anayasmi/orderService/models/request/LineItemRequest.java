package com.anayasmi.orderService.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineItemRequest {

    private String itemName;   //To store product id.
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String sku;

}
