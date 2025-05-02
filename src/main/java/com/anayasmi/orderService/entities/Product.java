package com.anayasmi.orderService.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(name = "rating_rate")
    private Double ratingRate;

    @Column(name = "rating_count")
    private Integer ratingCount;
}
