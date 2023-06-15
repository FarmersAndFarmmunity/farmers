package com.shop.farmers.boundedContext.review.repository;

import com.shop.farmers.boundedContext.item.constant.ItemSellStatus;
import com.shop.farmers.boundedContext.item.entity.Item;
import com.shop.farmers.boundedContext.item.repository.ItemRepository;
import com.shop.farmers.boundedContext.member.dto.MemberFormDto;
import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.repository.MemberRepository;
import com.shop.farmers.boundedContext.review.dto.ReviewFormDto;
import com.shop.farmers.boundedContext.review.entity.Review;
import com.shop.farmers.boundedContext.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ReviewRepositoryTest {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ItemRepository itemRepository;

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

    public Review createReview(Member member, Item item, int num) {


        ReviewFormDto reviewFormDto = new ReviewFormDto();
        reviewFormDto.setItem(item);
        reviewFormDto.setItemId(item.getId());
        reviewFormDto.setContents("엄마가 카라향 좋아하셔서 구입해봤는데맛 좋았어요!!!온라인으로 과일류 구입하는거 안보고 사는거라 좀 걱정하는 편인데 파머스 믿고 삽니다" + num);
        Review review = reviewFormDto.createReview(member);
        reviewRepository.save(review);
        return review;
    }

    public

    @Test
    @DisplayName("상품 id에 해당하는 리뷰 가져오기")
    void t001() {
        Item item = saveItem();
        Member member = saveMember();
        Review review = createReview(member, item, 1); // 리뷰 작성

        List<Review> reviewList = reviewRepository.findByItemIdLike(item.getId()); // 리뷰 작성한 상품의 id로부터 리뷰목록 조회
        Review review1 = reviewList.get(0);  // 리뷰 목록 중 첫번 째(방금 작성한 리뷰)를 가져옴
        assertThat(item.getId()).isEqualTo(review1.getItem().getId()); // 리뷰 작성한 상품 id와 해당 상품 id에 리뷰가 달린 상품id와 대조
    }

    @Test
    @DisplayName("리뷰 여러 개를 잘 가져오는지 테스트")
    void t002() {
        Item item = saveItem();
        Member member = saveMember();
        Review review1 = createReview(member, item, 1); // 1번 리뷰 작성
        Review review2 = createReview(member, item, 2); // 2번 리뷰 작성

        List<Review> reviewList = reviewRepository.findByItemIdLike(item.getId());
        Review review = reviewList.get(1); // 리뷰목록 중 2번 리뷰를 가져옴
        assertThat(review.getContents()).isEqualTo("엄마가 카라향 좋아하셔서 구입해봤는데맛 좋았어요!!!온라인으로 과일류 구입하는거 안보고 사는거라 좀 걱정하는 편인데 파머스 믿고 삽니다2");
    }
}