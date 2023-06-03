package com.shop.farmers.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;  //주문가격

    private int count;  //수량

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 주문한 아이템을 지정
        orderItem.setCount(count); // 주문한 아이템을 몇 개를 살지
        orderItem.setOrderPrice(item.getPrice()); // 해당 상품의 가격을 저장

        item.removeStock(count); // 총 아이템의 재고 갯수에서 주문한 상품의 갯수 만큼 제거
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }

}