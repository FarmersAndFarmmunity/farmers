package com.shop.farmers.boundedContext.review.repository;

import com.shop.farmers.boundedContext.review.dto.ReviewSearchDto;
import com.shop.farmers.boundedContext.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<Review> getMyReviewPage(ReviewSearchDto reviewSearchDto, Pageable pageable, Long id);
}
