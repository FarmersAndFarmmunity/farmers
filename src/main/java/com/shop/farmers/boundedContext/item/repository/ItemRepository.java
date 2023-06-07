package com.shop.farmers.boundedContext.item.repository;

import com.shop.farmers.boundedContext.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    void deleteById(long itemId);
    List<Item> findByCreatedBy(String email);
}
