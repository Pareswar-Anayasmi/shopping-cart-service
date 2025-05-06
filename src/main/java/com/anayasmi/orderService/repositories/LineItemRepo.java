package com.anayasmi.orderService.repositories;

import com.anayasmi.orderService.entities.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LineItemRepo extends JpaRepository<LineItem,Long> {

    List<LineItem>  findByOrder_OrderId (Long OrderId) ;
}
