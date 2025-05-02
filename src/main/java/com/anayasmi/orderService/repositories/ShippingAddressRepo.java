package com.anayasmi.orderService.repositories;

import com.anayasmi.orderService.entities.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepo  extends JpaRepository<ShippingAddress,Long> {
}
