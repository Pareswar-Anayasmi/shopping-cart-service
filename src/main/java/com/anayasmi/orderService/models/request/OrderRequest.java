package com.anayasmi.orderService.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String orderStatus;
    private BigDecimal orderTotal;
    private String paymentMethod;
    private String shippingMethod;
    private BigDecimal shippingCost;
    private String notes;
    private CustomerRequest customer;
    private DiscountRequest discount;
    private TaxRequest tax;
    private ShippingAddressRequest shippingAddress;
    private BillingAddressRequest billingAddress;
    private List<LineItemRequest> items;
}
