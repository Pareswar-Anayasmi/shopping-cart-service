package com.anayasmi.orderService.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TAXES")
public class Tax {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="tax_id",nullable = false)
        private Long taxId;
        private BigDecimal rate;
        private BigDecimal amount;

//        @OneToMany(fetch = FetchType.LAZY ,mappedBy = "tax")
//        private List<Order> order;
}
