package com.anayasmi.orderService.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
    @Table(name = "ORDERS")
    public class Order {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "order_id" ,nullable = false)
        private Long orderId;

        @Column(name = "order_custom_id",nullable = false)
        private String orderCustomId;

        @Column(name = "order_date")
        private LocalDateTime orderDate;

        @Column(name = "order_status")
        private String orderStatus;

        @Column(name = "order_total")
        private BigDecimal orderTotal;

        @Column(name = "payment_method")
        private String paymentMethod;

        @Column(name = "shipping_method")
        private String shippingMethod;

        @Column(name = "shipping_cost")
        private BigDecimal shippingCost;

        @Column(name = "notes")
        private String notes;

        @Column(name = "created_by")
        private String createdBy;

        @Column(name = "created_date")
        private LocalDateTime createdDate;

        @Column(name = "updated_by")
        private String updatedBy;

        @Column(name = "updated_date")
        private LocalDateTime updatedDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "customer_id")
        private Customer customer;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "shipping_address_id")
        private ShippingAddress shippingAddress;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "billing_address_id")
        private BillingAddress billingAddress;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "discount_id")
        private Discount discount;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tax_id")
        private Tax tax;

        @OneToMany(fetch = FetchType.LAZY,mappedBy = "order")
        private List<LineItem> lineItems;

}
