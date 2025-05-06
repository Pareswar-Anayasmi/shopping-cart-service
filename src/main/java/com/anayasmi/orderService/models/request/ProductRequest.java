package com.anayasmi.orderService.models.request;

import lombok.*;
import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String productName;
    private String description;
    private BigDecimal price;
}
