package com.anayasmi.orderService.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMERS")
public class Customer {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "customer_id",nullable = false)
        private Long customerId;
        @Column(name = "first_name")
        private String firstName;
        @Column(name = "last_name")
        private String lastName;
        private String email;
        private String phone;

//        @OneToMany(fetch = FetchType.LAZY ,mappedBy = "customer")
//        private List<Order> orders;

}
