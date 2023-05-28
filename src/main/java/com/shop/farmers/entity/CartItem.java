package com.shop.farmers.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
public class CartItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne // 하나의 장바구니에는 여러 개의 상품을 담을 수 있음
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne // 하나의 상품은 여러 개의 장바구니 상품에 담길 수 있음
    @JoinColumn(name = "item_id")
    private Item item; // 장바구니에 담을 상품의 정보를 알아야 함

    private int count; // 같은 상품을 장바구니에 몇개를 담을지를 지정
}
