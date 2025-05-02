package com.anayasmi.orderService.services;

import com.anayasmi.orderService.models.request.OrderRequest;
import com.anayasmi.orderService.models.response.*;

import java.util.List;

public interface OrderService {

    List<ProductResponse> fetchProducts();
    List<ProductResponse> fetchProductsBYCategory(String category);

    ProductResponse fetchProduct(Long productId);
    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse approveRejectOrder(Long id, String action);

    List<OrderResponse> fetchAllOrder ();
    OrderResponse fetchOrderById(Long orderId);

}
