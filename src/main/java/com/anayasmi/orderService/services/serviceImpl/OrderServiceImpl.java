package com.anayasmi.orderService.services.serviceImpl;

import com.anayasmi.orderService.entities.*;
import com.anayasmi.orderService.mapper.OrderServiceMapper;
import com.anayasmi.orderService.models.request.OrderRequest;
import com.anayasmi.orderService.models.response.*;
import com.anayasmi.orderService.repositories.*;
import com.anayasmi.orderService.services.OrderService;
import com.anayasmi.orderService.services.kafkaService.KafkaFileProcessService;
import com.anayasmi.orderService.services.kafkaService.OrderStatusProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductRepo productRepo ;

    private final OrderServiceMapper orderServiceMapper ;

    private final CustomerRepo customerRepo;

    private final DiscountRepo discountRepo;

    private final TaxRepo taxRepo;

    private final OrderRepository orderRepository;

    private final LineItemRepo lineItemRepo;

    private final BillingAddressRepo billingAddressRepo;

    private final ShippingAddressRepo shippingAddressRepo;

    private final OrderStatusProducerService orderStatusProducerService;

    private final KafkaFileProcessService kafkaFileProcessService;

    public String generateShortOrderId() {
        String prefix = "ORD";
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMM"));
        String randomPart = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
        return prefix + "-" + datePart + randomPart;
    }

    @Transactional
    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        try {

            ShippingAddress shippingAddress = (shippingAddressRepo.save(orderServiceMapper.shippingMap(orderRequest.getShippingAddress())));
            BillingAddress billingAddress = (billingAddressRepo.save(orderServiceMapper.billingMap(orderRequest.getBillingAddress())));
            Customer customer = customerRepo.save(orderServiceMapper.map(orderRequest.getCustomer()));
            Discount discount = discountRepo.save(orderServiceMapper.map(orderRequest.getDiscount()));
            Tax tax = taxRepo.save(orderServiceMapper.map(orderRequest.getTax()));

            Order order= orderServiceMapper.mapOrder(orderRequest,generateShortOrderId(),shippingAddress,billingAddress,
            customer,discount,tax);

            Order insertedOrder = orderRepository.save(order);
            // Save line items
            List<LineItem> lineItems = new ArrayList<>();
            orderRequest.getItems().forEach(lineItemRequest -> {
                LineItem lineItem = orderServiceMapper.mapLineItem(lineItemRequest);
                lineItem.setOrder(insertedOrder);
                lineItems.add(lineItem);
            });

            order.setLineItems(lineItemRepo.saveAll(lineItems));
            log.info("Order created successfully with OrderCustomId: {}", order.getOrderCustomId());

            OrderResponse orderResponse = orderServiceMapper.mapOrder(order);

            //this method will store the response as a JSON.
            this.saveOrderResponseToFile(orderResponse);

            return orderResponse;

        } catch (Exception ex) {
            log.error("Error occurred while creating order: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to create order", ex);
        }
    }

    private void saveOrderResponseToFile(OrderResponse order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 1. Create formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");

            // 2. Create JavaTimeModule and add serializer with formatter
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));

            // 3. Register the module
            objectMapper.registerModule(javaTimeModule);

            // 4. Disable timestamps
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // 5. Save file
            String filename = "orders/order_" + order.getOrderCustomId() + "_" + System.currentTimeMillis() + ".json";
            File file = new File(filename);
            file.getParentFile().mkdirs(); // Ensure "orders/" folder is created
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, order);

            //this will send the file to kafka.
            kafkaFileProcessService.sendJsonFileToKafka(filename);
            Path path = Paths.get(filename);
            try {
                // Delete the file if it exists
                 Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace(); // You can use a logger here
            }

            log.info("OrderResponse saved to file: {}", filename);
        } catch (Exception e) {
            log.error("Failed to save OrderResponse to file", e);
        }
    }

    public OrderResponse approveRejectOrder(Long id, String action) {
        try {
            Optional<Order> orderOptional = orderRepository.findByOrderId(id);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                order.setOrderStatus(action);
                return this.orderConverter( orderRepository.save(order));
            } else {
                // Handle when order is not found
                throw new RuntimeException("Order not found with ID: " + id);
            }

        } catch (Exception ex) {
            log.error("Error occurred while updating order: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to update order", ex);
        }
    }
    @Override
    public List<OrderResponse> fetchAllOrder() {
        try {
            List<Order> orders = orderRepository.findAll();
            // If no orders are found, log it
            if (orders.isEmpty()) {
                log.warn("No orders found in the database.");
            }

            log.info("Fetched {} orders successfully.", orders.size());
            List<OrderResponse> orderResponses = new ArrayList<>();
            orders.forEach(order -> {
                orderResponses.add(this.orderConverter(order));
            });
            return orderResponses;

        } catch (Exception ex) {
            log.error("Error occurred while fetching all orders: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch orders", ex);  // Propagate exception to the controller
        }
    }

    @Override
    public OrderResponse fetchOrderById(Long orderId) {
        try {
            Optional<Order> orderOptional = orderRepository.findByOrderId(orderId);

            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                return this.orderConverter(order);
            } else {
                // Handle when order is not found
                throw new RuntimeException("Order not found with ID: " + orderId);
            }
        } catch (Exception e) {
            // Log the error or rethrow with a meaningful message
            throw new RuntimeException("Failed to fetch order with ID: " + orderId, e);
        }
    }

    public OrderResponse orderConverter(Order order) {
        List<LineItemResponse> lineItemResponses = new ArrayList<>();
        if (order.getLineItems() != null) {
            order.getLineItems().forEach(lineItem -> {
                lineItemResponses.add(orderServiceMapper.lineItemMap(lineItem));
            });
        }

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderCustomId(order.getOrderCustomId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .orderTotal(order.getOrderTotal())
                .paymentMethod(order.getPaymentMethod())
                .shippingMethod(order.getShippingMethod())
                .shippingCost(order.getShippingCost())
                .notes(order.getNotes())
                .createdBy(order.getCreatedBy())
                .createdDate(order.getCreatedDate())
                .updatedBy(order.getUpdatedBy())
                .updatedDate(order.getUpdatedDate())
                .customer(orderServiceMapper.map(order.getCustomer()))
                .shippingAddress(orderServiceMapper.map(order.getShippingAddress()))
                .billingAddress(orderServiceMapper.map(order.getBillingAddress()))
                .discount(orderServiceMapper.map(order.getDiscount()))
                .tax(orderServiceMapper.map(order.getTax()))
                .lineItems(lineItemResponses)
                .build();
    }

    @Override
    public List<ProductResponse> fetchProducts() {
        try {
            List<Product> productList = productRepo.findAll();
            if (productList.isEmpty()) {
                log.warn("No products found in the database.");
                return List.of();  // return an empty list
            }
            List<ProductResponse> productResponses = orderServiceMapper.productMapperList(productList);
            log.info("Fetched {} products successfully.", productResponses.size());
            return productResponses;
        } catch (Exception ex) {
            log.error("Error occurred while fetching products: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch products", ex);
        }
    }

    @Override
    public List<ProductResponse> fetchProductsBYCategory(String category){
        try {
            List<Product> productList = productRepo.findByCategory(category);
            if (productList.isEmpty()) {
                log.warn("No products found in the database.");
                return List.of();  // return an empty list
            }
            List<ProductResponse> productResponses = orderServiceMapper.productMapperList(productList);
            log.info("Fetched {} products successfully.", productResponses.size());
            return productResponses;
        } catch (Exception ex) {
            log.error("Error occurred while fetching products: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch products", ex);
        }
    }

    @Override
    public ProductResponse fetchProduct(Long productId) {
        try {
            Optional<Product> productOptional = productRepo.findById(productId);
            if (productOptional.isPresent()) {
                log.info("Product with ID {} found.", productId);
                return orderServiceMapper.productMapper(productOptional.get());
            } else {
                log.warn("Product with ID {} not found.", productId);
                return null;
            }
        } catch (Exception ex) {
            log.error("Error occurred while fetching product with ID {}: {}", productId, ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch product", ex);
        }
    }

}
