package com.shop.farmers.entity;

import com.shop.farmers.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders") // "order"는 예약어이기 때문에 따로 지정
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 한 명의 회원은 여러 번 주문을 할 수 있음
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY) // 하나의 주문이 여러 개의 주문 상품을 가짐
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        OrderItem.builder().order(this).build();
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .build();

        for(OrderItem orderItem : orderItemList){ // 하나의 orderItem 에 하나의 주문을 매핑시킨다.
            order.addOrderItem(orderItem);
        }

        return order;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
