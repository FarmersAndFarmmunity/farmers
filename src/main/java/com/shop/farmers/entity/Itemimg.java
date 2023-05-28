package com.shop.farmers.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "item_img")
@Getter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor
public class Itemimg extends BaseEntity{

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; // 이미지 파일명

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로

    private String repimgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 상품은 여러 개의 이미지를 가짐
    @JoinColumn(name = "item_id")
    private Item item;
}
