package com.shop.farmers.boundedContext.item.dto;

import com.shop.farmers.boundedContext.item.constant.ItemClassifyStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemClassifyDto {
    private ItemClassifyStatus itemClassifyStatus;

    public void setItemClassifyStatus(ItemClassifyStatus itemClassifyStatus) {
        this.itemClassifyStatus = itemClassifyStatus;
    }
}
