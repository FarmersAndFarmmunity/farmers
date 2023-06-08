package com.shop.farmers.boundedContext.review.repository;

import com.shop.farmers.boundedContext.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
