package com.shop.farmers.repository;

import com.shop.farmers.dto.ItemSearchDto;
import com.shop.farmers.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
