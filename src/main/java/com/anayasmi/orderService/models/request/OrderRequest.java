package com.anayasmi.orderService.models.request;

import com.anayasmi.orderService.entities.Customer;
import com.anayasmi.orderService.entities.Discount;
import com.anayasmi.orderService.entities.Tax;
import com.anayasmi.orderService.models.response.CustomerResponse;
import com.anayasmi.orderService.models.response.DiscountResponse;
import com.anayasmi.orderService.models.response.TaxResponse;
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
