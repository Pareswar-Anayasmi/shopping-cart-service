package com.anayasmi.orderService.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxResponse {

    private Long taxId;
    private BigDecimal rate;
    private BigDecimal amount;
}
