package com.shop.farmers.boundedContext.review.entity;

import com.shop.farmers.boundedContext.item.entity.Item;
import com.shop.farmers.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String contents;

    // 회원과 다대일 연관관계 -> 회원은 여러 개의 리뷰를 달 수 있다.
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 상품과 다대일 연관관계 -> 하나의 상품에는 여러 개의 리뷰가 연결될 수 있다.
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
