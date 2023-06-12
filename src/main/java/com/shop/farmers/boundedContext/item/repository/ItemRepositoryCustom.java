package com.shop.farmers.boundedContext.item.repository;

import com.shop.farmers.boundedContext.item.dto.ItemSearchDto;
import com.shop.farmers.boundedContext.item.dto.MainItemDto;
import com.shop.farmers.boundedContext.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<Item> getMyItemPage(ItemSearchDto itemSearchDto, Pageable pageable, String email);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);


}
