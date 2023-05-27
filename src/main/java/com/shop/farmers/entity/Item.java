package com.shop.farmers.entity;


import com.shop.farmers.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
//@Table(name = "item") // 어차피 알아서 Item 클래스 이름으로 생성을 해주기 때문에 주석 처리
@Getter
@Setter
@ToString
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemName; // 상품명

    @Column(name = "price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stock; // 재고 수량

    @Lob
    @Column(nullable = false)
    private String description; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    @CreatedDate // insert 할 때마다 자동 생성
    private LocalDateTime regDate;
    @LastModifiedDate // update 시 계속 로딩
    private LocalDateTime updateDate;
}
