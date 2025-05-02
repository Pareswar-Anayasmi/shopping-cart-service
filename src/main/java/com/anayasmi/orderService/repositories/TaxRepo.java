package com.anayasmi.orderService.repositories;

import com.anayasmi.orderService.entities.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TaxRepo extends JpaRepository<Tax,Long> {
}
