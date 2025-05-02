package com.anayasmi.orderService.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
    @Table(name = "LINE_ITEM")
    public class LineItem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "line_item_id",nullable = false)
        private Long lineItemId;

        @Column(name = "item_name")
        private String itemName;

        private Integer quantity;

        @Column(name = "unit_Price")
        private BigDecimal unitPrice;

        @Column(name = "total_price")
        private BigDecimal totalPrice;
        private String sku;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id")
        private Order order;
}
