package com.shop.farmers.boundedContext.order.service;

import com.shop.farmers.boundedContext.item.constant.ItemSellStatus;
import com.shop.farmers.boundedContext.item.entity.Item;
import com.shop.farmers.boundedContext.item.repository.ItemRepository;
import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.repository.MemberRepository;
import com.shop.farmers.boundedContext.order.constant.OrderStatus;
import com.shop.farmers.boundedContext.order.dto.OrderDto;
import com.shop.farmers.boundedContext.order.entity.Order;
import com.shop.farmers.boundedContext.order.repository.OrderRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    @DisplayName("주문 취소 테스트")
    public void cencelOrder() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail()); // 주문 데이터 생성

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new); // 생성한 주문 엔티티 조회
        orderService.cancelOrder(orderId); // 해당 주문 취소

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus()); // 취소 상태라면 통과
        assertEquals(100, item.getStockNumber()); // 취소 후 상품 재고가 처음 개수인 100개와 같으면 통과
    }
}