package com.shop.farmers.service;

import com.shop.farmers.constant.ItemSellStatus;
import com.shop.farmers.dto.OrderDto;
import com.shop.farmers.entity.Item;
import com.shop.farmers.entity.Member;
import com.shop.farmers.entity.Order;
import com.shop.farmers.entity.OrderItem;
import com.shop.farmers.repository.ItemRepository;
import com.shop.farmers.repository.MemberRepository;
import com.shop.farmers.repository.OrderRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // application-test.yml 을 활성화 시킨다.
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional
class OrderServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    void t001() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto(); // 주문을 생성
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityExistsException::new); // 주문 번호로 조회해서 주문을 찾음

//        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = orderDto.getCount() * item.getPrice(); // 계산한 총 물건의 합

        assertThat(totalPrice).isEqualTo(order.getTotalPrice()); // 계산한 총 물건의 합 == order.totalPrice 이 같아야 한다
    }
}