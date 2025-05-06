package com.anayasmi.orderService.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxRequest {

    private BigDecimal rate;
    private BigDecimal amount;
}
