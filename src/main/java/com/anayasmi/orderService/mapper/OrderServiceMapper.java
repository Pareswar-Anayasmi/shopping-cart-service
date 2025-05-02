package com.anayasmi.orderService.mapper;

import com.anayasmi.orderService.entities.*;
import com.anayasmi.orderService.models.request.*;
import com.anayasmi.orderService.models.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderServiceMapper {

    ProductResponse productMapper(Product product);
    List<ProductResponse> productMapperList(List<Product> productList);
    @Mapping(target = "billingAddressId",ignore = true)
    BillingAddress billingMap(BillingAddressRequest billingAddressRequest);
    @Mapping(target = "shippingAddressId",ignore = true)
    ShippingAddress shippingMap(ShippingAddressRequest shippingAddressRequest);
    CustomerResponse map(Customer customer);
    @Mapping(target = "lineItemId",ignore = true)
    LineItem mapLineItem(LineItemRequest lineItemRequest);

    ShippingAddressResponse map(ShippingAddress value);
    BillingAddressResponse map(BillingAddress value);

    DiscountResponse map (Discount discount);
    List<DiscountResponse> mapList (List<Discount> discount);

    TaxResponse map (Tax tax);
    List<TaxResponse> map (List<Tax> tax);

    OrderResponse mapOrder(Order order);
    List<OrderResponse> mapOrder(List<Order> order);

    List<LineItemResponse> lineItemMap(List<LineItem> lineItem);
    LineItemResponse lineItemMap(LineItem lineItem);
    @Mapping(target = "customerId",ignore = true)
    Customer map(CustomerRequest customerRequest);
    @Mapping(target = "discountId",ignore = true)
    Discount map(DiscountRequest discountRequest);
    @Mapping(target = "taxId",ignore = true)
    Tax map(TaxRequest taxRequest);

    default Order mapOrder(OrderRequest orderRequest, String orderId, ShippingAddress shippingAddress, BillingAddress billingAddress,
                           Customer customer,Discount discount,Tax tax){

        return Order.builder()
                .orderStatus(orderRequest.getOrderStatus())
                .orderTotal(orderRequest.getOrderTotal())
                .paymentMethod(orderRequest.getPaymentMethod())
                .shippingMethod(orderRequest.getShippingMethod())
                .shippingCost(orderRequest.getShippingCost())
                .notes(orderRequest.getNotes())
                .orderDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .createdBy("ADMIN")
                .orderCustomId(orderId)
                .shippingAddress(shippingAddress)
                .billingAddress(billingAddress)
                .customer(customer)
                .discount(discount)
                .tax(tax)
                .build();
    }
}
