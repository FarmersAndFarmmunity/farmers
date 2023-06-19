package com.shop.farmers.boundedContext.review.service;

import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.service.MemberService;
import com.shop.farmers.boundedContext.order.entity.Order;
import com.shop.farmers.boundedContext.order.entity.OrderItem;
import com.shop.farmers.boundedContext.order.service.OrderService;
import com.shop.farmers.boundedContext.review.dto.ReviewFormDto;
import com.shop.farmers.boundedContext.review.dto.ReviewSearchDto;
import com.shop.farmers.boundedContext.review.entity.Review;
import com.shop.farmers.boundedContext.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;

    private final OrderService orderService;

    public boolean validateOrder(String email, Long orderId, Long itemId) {
        Member curMember = memberService.findByEmail(email);
        Order order = orderService.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        boolean checkFlag = false; // 주문에 품목이 있는지

        for (OrderItem orderItem : order.getOrderItems()) {
            Long id = orderItem.getItem().getId();
            if (itemId == id) {
                checkFlag = true;
                break; // 일치하는 것을 찾았다면 종료
            }
        }

        // 현재 멤버 아이디와 주문의 멤버 아이디가 일치하고 주문에 해당 상품이 있는지 검사
        if (curMember.getId() != order.getMember().getId() || !checkFlag) {
            return false;
        }

        return true;
    }

    public Long saveReview(ReviewFormDto reviewFormDto, Member member) {
        Review review = reviewFormDto.createReview(member);
        reviewRepository.save(review);

        return review.getId();
    }

    public List<Review> getList(Long itemId) {
        return reviewRepository.findByItemIdLike(itemId);
    }

    // 리뷰 관리 페이지에서 리뷰를 가져오는 로직
    public Page<Review> getReviewPage(ReviewSearchDto reviewSearchDto, Pageable pageable, String email) {
        Member member = memberService.findByEmail(email);
        Long id = member.getId();
        return reviewRepository.getMyReviewPage(reviewSearchDto, pageable, id);
    }

    public void deleteReview(Long reviewId) {
        Optional<Review> opReview = reviewRepository.findById(reviewId);

        if (opReview.isPresent()) {
            reviewRepository.delete(opReview.get());
        }
    }
}
