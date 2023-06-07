package com.shop.farmers.boundedContext.order.repository;

import com.shop.farmers.boundedContext.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
