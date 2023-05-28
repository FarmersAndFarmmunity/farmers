package com.shop.farmers.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne // 하나의 상품은 여러 개의 장바구니 상품에 담길 수 있음
    @JoinColumn(name = "item_id")
    private Item item;

    private int count; // 같은 상품을 장바구니에 몇개를 담을지를 지정
}
