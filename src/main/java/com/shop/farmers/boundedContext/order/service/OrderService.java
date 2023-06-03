package com.shop.farmers.boundedContext.order.service;

import com.shop.farmers.boundedContext.item.entity.Item;
import com.shop.farmers.boundedContext.item.entity.ItemImg;
import com.shop.farmers.boundedContext.item.repository.ItemImgRepository;
import com.shop.farmers.boundedContext.item.repository.ItemRepository;
import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.repository.MemberRepository;
import com.shop.farmers.boundedContext.order.dto.OrderDto;
import com.shop.farmers.boundedContext.order.dto.OrderHistDto;
import com.shop.farmers.boundedContext.order.dto.OrderItemDto;
import com.shop.farmers.boundedContext.order.entity.Order;
import com.shop.farmers.boundedContext.order.entity.OrderItem;
import com.shop.farmers.boundedContext.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()) // 주문할 상품 조회
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원의 이메일로 회원 정보 조회

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티 생성
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList); // 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티를 생성
        orderRepository.save(order); // 생성한 주문 엔티티를 저장

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) { // 현재 로그인한 사용자와 주문한 사용자가 같은지 검사
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder(); // 주문 취소 하면 변경 감지 기능에 의해서 트랜잭션이 끝날 때 update 쿼리가 실행됨
    }
}
