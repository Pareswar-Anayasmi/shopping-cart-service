package com.anayasmi.orderService.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
