package com.anayasmi.orderService.models.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
