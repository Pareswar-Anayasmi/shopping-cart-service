package com.anayasmi.orderService.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private boolean success;
    private String message;
    private Object data;

}
