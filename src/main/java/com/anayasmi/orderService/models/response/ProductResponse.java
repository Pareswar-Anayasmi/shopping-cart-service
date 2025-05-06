package com.anayasmi.orderService.models.response;

import lombok.*;
import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String image;
    private Double ratingRate;
    private Integer ratingCount;
}
