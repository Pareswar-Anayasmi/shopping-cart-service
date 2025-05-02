package com.anayasmi.orderService.entities;

import jakarta.persistence.*;
import lombok.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BILLING_ADDRESS")
public class BillingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billing_address_id",nullable = false)
    private Long billingAddressId;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_Line_2")
    private String addressLine2;

    private String city;
    private String state;
    @Column(name = "zip_code")
    private String zipCode;
    private String country;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "billingAddress",cascade = CascadeType.ALL)
//    private List<Order> orders;

}
