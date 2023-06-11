package com.shop.farmers.boundedContext.order.repository;

import com.shop.farmers.boundedContext.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByItemIdAndCreatedBy(Long itemId, String email);
}
