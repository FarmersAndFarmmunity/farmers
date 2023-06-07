package com.shop.farmers.boundedContext.order.dto;

import com.shop.farmers.boundedContext.order.constant.OrderStatus;
import com.shop.farmers.boundedContext.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderDtlDto {

    public OrderDtlDto(Order order){
        this.orderId = order.getId();
        this.customerName = order.getMember().getName();
        this.orderName = order.makeName();
        this.totalPrice = order.getTotalPrice();
    }

    private Long orderId; //주문아이디

    private String customerName; // 주문자 이름

    private String orderName; // 주문명

    private List<OrderItemDtlDto> orderItemDtlDtos = new ArrayList<>(); //주문상품리스트

    private int totalPrice; // 총주문 금액

    public void addOrderItemDto(OrderItemDtlDto orderItemDtlDto){
        orderItemDtlDtos.add(orderItemDtlDto);
    }

}