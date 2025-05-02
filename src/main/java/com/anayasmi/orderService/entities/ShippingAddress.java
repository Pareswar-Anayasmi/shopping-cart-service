package com.anayasmi.orderService.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SHIPPING_ADDRESS")
public class ShippingAddress {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "shipping_address_id",nullable = false)
        private Long shippingAddressId;

        @Column(name = "address_line_1")
        private String addressLine1;

        @Column(name = "address_Line_2")
        private String addressLine2;
        private String city;
        private String state;
        private String zipCode;
        private String country;

//        @OneToMany(fetch = FetchType.LAZY ,mappedBy = "shippingAddress",cascade = CascadeType.ALL)
//        private List<Order> order;
}
