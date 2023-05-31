package com.shop.farmers.repository;

import com.shop.farmers.constant.ItemSellStatus;
import com.shop.farmers.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
