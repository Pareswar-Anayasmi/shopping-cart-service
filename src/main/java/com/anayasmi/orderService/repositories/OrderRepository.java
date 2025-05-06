package com.anayasmi.orderService.repositories;

import com.anayasmi.orderService.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.lineItems " +
            "LEFT JOIN FETCH o.customer " +
            "LEFT JOIN FETCH o.shippingAddress " +
            "LEFT JOIN FETCH o.billingAddress " +
            "LEFT JOIN FETCH o.discount " +
            "LEFT JOIN FETCH o.tax " +
            "WHERE o.orderId = :orderId")
    Optional<Order> findByOrderId(@Param("orderId") Long orderId);
}
