package com.shop.farmers.boundedContext.review.controller;

import com.shop.farmers.boundedContext.review.dto.ReviewFormDto;
import com.shop.farmers.boundedContext.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = {"/items/{itemId}/reviews/new"})
    public String showReviewForm(@PathVariable("itemId") Long itemId, Principal principal, Model model){
        // 해당 회원이 상품을 실제 구매를 한 회원인지 체크해야 함
        model.addAttribute("reviewFormDto", new ReviewFormDto());
        return "/review/reviewForm";
    }

    @PostMapping(value = {"/items/{itemId}/reviews"})
    public String itemDtl(@PathVariable("itemId") Long itemId, Principal principal, ReviewFormDto dto){
        String email = principal.getName(); // 회원 정보를 가져옴
        String contents = dto.getContents();
        return "redirect:/";
    }
}
