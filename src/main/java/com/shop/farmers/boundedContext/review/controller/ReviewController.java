package com.shop.farmers.boundedContext.review.controller;

import com.shop.farmers.boundedContext.item.entity.Item;
import com.shop.farmers.boundedContext.item.service.ItemService;
import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.service.MemberService;
import com.shop.farmers.boundedContext.review.dto.ReviewFormDto;
import com.shop.farmers.boundedContext.review.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ItemService itemService;

    private final MemberService memberService;

    @Transactional(readOnly = true)
    @GetMapping(value = {"/reviews/new"})
    public String showReviewForm(Long itemId, Long orderId, Principal principal, Model model){
        String email = principal.getName(); // 로그인한 회원 정보
        // 해당 회원이 상품을 실제 구매를 한 회원인지 체크해야 함
        if (!(reviewService.validateOrder(email, orderId, itemId))) {
            return "redirect:/orders"; // 권한이 없으면 구매이력으로 매핑
        }

        model.addAttribute("reviewFormDto", new ReviewFormDto());

        return "review/reviewForm";
    }

    @Transactional
    @PostMapping(value = {"/reviews/new"})
    public String createReview(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult,
                               Principal principal, Model model){

        if(bindingResult.hasErrors()){
            return "review/reviewForm";
        }

        try {
            reviewService.saveReview(reviewFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", "리뷰 등록 중 에러가 발생하였습니다.");
            return "review/reviewForm";
        }

        return "redirect:/";
    }
}
