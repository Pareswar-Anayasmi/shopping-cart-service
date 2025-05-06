package com.anayasmi.orderService.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DISCOUNTS")

public class Discount {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "discount_id",nullable = false)
        private Long discountId;
        private String code;
        private BigDecimal amount;

//        @OneToMany(fetch = FetchType.LAZY ,mappedBy = "discount")
//        private List<Order> order;
}
