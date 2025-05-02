package com.anayasmi.orderService.models.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private String orderCustomId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private BigDecimal orderTotal;
    private String paymentMethod;
    private String shippingMethod;
    private CustomerResponse customer;
    private ShippingAddressResponse shippingAddress;
    private BillingAddressResponse billingAddress;
    private List<LineItemResponse> lineItems;
    private BigDecimal shippingCost;
    private String notes;
    private DiscountResponse discount;
    private TaxResponse tax;
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;

}

