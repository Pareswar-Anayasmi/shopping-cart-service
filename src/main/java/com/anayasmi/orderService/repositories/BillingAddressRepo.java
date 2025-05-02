package com.anayasmi.orderService.repositories;

import com.anayasmi.orderService.entities.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingAddressRepo  extends JpaRepository<BillingAddress,Long> {
}
