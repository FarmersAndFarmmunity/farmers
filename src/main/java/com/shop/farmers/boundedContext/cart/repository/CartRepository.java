package com.shop.farmers.boundedContext.cart.repository;

import com.shop.farmers.boundedContext.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId);
}
