package com.anayasmi.orderService.controllers;

import com.anayasmi.orderService.models.request.OrderRequest;
import com.anayasmi.orderService.models.response.*;
import com.anayasmi.orderService.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( "/v1" )
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            OrderResponse orderResponse = orderService.createOrder(orderRequest);
            if (orderResponse != null) {
                log.info("Order created successfully with Order ID: {}", orderResponse.getOrderId());
                return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
            } else {
                log.warn("Failed to create order.");
                return new ResponseEntity<>("Failed to create order.", HttpStatus.BAD_GATEWAY);
            }
        } catch (Exception ex) {
            log.error("Error occurred while creating order: {}", ex.getMessage(), ex);
            return new ResponseEntity<>("Failed to create order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/order/{id}/{action}")
    public ResponseEntity<?> approveRejectOrder(@PathVariable Long id,@PathVariable String action) {
        try {
            OrderResponse orderResponse = orderService.approveRejectOrder(id,action);
            if (orderResponse != null) {
                log.info("Order update successfully with Order ID: {}", id);
                return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
            } else {
                log.warn("Failed to update order.");
                return new ResponseEntity<>("Failed to update order.", HttpStatus.BAD_GATEWAY);
            }
        } catch (Exception ex) {
            log.error("Error occurred while updating order: {}", ex.getMessage(), ex);
            return new ResponseEntity<>("Failed to update order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/products")
    public ResponseEntity<?> fetchProducts() {
        try {
            List<ProductResponse> productResponses = orderService.fetchProducts();
            log.info("Fetched {} products successfully.", productResponses.size());
            return new ResponseEntity<>(productResponses, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error occurred while fetching products: {}", ex.getMessage(), ex);
            return new ResponseEntity<>("Failed to fetch products.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/category/{category}")
    public ResponseEntity<?> fetchProductsBYCategory(@PathVariable String category) {
        try {
            List<ProductResponse> productResponses = orderService.fetchProductsBYCategory(category);
            log.info("Fetched {} products successfully.", productResponses.size());
            return new ResponseEntity<>(productResponses, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error occurred while fetching products: {}", ex.getMessage(), ex);
            return new ResponseEntity<>("Failed to fetch products.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> fetchProductById(@PathVariable Long id) {
        try {
            ProductResponse productResponse = orderService.fetchProduct(id);
            if (productResponse != null) {
                log.info("Product with ID {} fetched successfully.", id);
                return new ResponseEntity<>(productResponse, HttpStatus.OK);
            } else {
                log.warn("Product with ID {} not found.", id);
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Error occurred while fetching product with ID {}: {}", id, ex.getMessage(), ex);
            return new ResponseEntity<>("Failed to fetch product.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> fetchAllOrder() {
        try {
            List<OrderResponse> orderResponses = orderService.fetchAllOrder();

            if (orderResponses.isEmpty()) {
                log.warn("No orders found.");
                return new ResponseEntity<>("No orders found.", HttpStatus.NOT_FOUND);  // 404 if empty
            }

            log.info("Fetched {} orders successfully.", orderResponses.size());
            return new ResponseEntity<>(orderResponses, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error occurred while fetching orders: {}", ex.getMessage(), ex);
            return new ResponseEntity<>("Failed to fetch orders", HttpStatus.INTERNAL_SERVER_ERROR);  // 500 on error
        }
    }

    @GetMapping("/order")
    public ResponseEntity<?> fetchOrderByOrderId(@RequestParam Long orderId) {
        try {
            // Fetch a single order by ID
            OrderResponse orderResponse = orderService.fetchOrderById(orderId);

            if (orderResponse == null) {
                log.warn("Order with ID {} not found.", orderId);
                return new ResponseEntity<>("Order not found.", HttpStatus.NOT_FOUND);  // 404 if order not found
            }

            log.info("Fetched order with ID {} successfully.", orderId);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error occurred while fetching order with ID {}: {}", orderId, ex.getMessage(), ex);
            return new ResponseEntity<>("Failed to fetch order", HttpStatus.INTERNAL_SERVER_ERROR);  // 500 on error
        }
    }
}
