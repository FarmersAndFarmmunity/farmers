package com.shop.farmers.repository;

import com.shop.farmers.constant.ItemSellStatus;
import com.shop.farmers.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // application-test.yml 을 활성화 시킨다.
@TestMethodOrder(MethodOrderer.MethodName.class)
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    void t001() {
        for (int i = 0; i < 10; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품 " + i)
                    .price(10000)
                    .stockNumber(100)
                    .itemDetail("테스트 상품 상세 설명")
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();

            itemRepository.save(item);
        }

        Optional<Item> opItem = itemRepository.findById(0L);

        if (opItem.isPresent()) {
            Item item = opItem.get();

            assertThat(item.getItemNm()).isEqualTo("테스트 상품 0");
        }
    }
}