package com.shop.farmers.boundedContext.review.repository;

import com.shop.farmers.boundedContext.review.entity.Review;
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

    @Test
    @DisplayName("상품 id에 해당하는 리뷰 가져오기")
    void t001() {
        List<Review> reviewList = reviewRepository.findByItemIdLike(3L);
        Review review = reviewList.get(0);
        assertThat(review.getItem().getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("리뷰 여러 개를 잘 가져오는지 테스트")
    void t002() {
        List<Review> reviewList = reviewRepository.findByItemIdLike(3L);
        Review review = reviewList.get(1);
        assertThat(review.getContents()).isEqualTo("엄마가 카라향 좋아하셔서 구입해봤는데맛 좋았어요!!!온라인으로 과일류 구입하는거 안보고 사는거라 좀 걱정하는 편인데 파머스 믿고 삽니다");
    }
}