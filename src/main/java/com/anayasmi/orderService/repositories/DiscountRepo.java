package com.anayasmi.orderService.repositories;

import com.anayasmi.orderService.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepo extends JpaRepository<Discount,Long> {
}
