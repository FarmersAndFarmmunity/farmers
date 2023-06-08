package com.shop.farmers.boundedContext.review.service;

import com.shop.farmers.boundedContext.item.dto.ItemFormDto;
import com.shop.farmers.boundedContext.item.service.ItemService;
import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.service.MemberService;
import com.shop.farmers.boundedContext.order.service.OrderService;
import com.shop.farmers.boundedContext.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;

    private final OrderService orderService;
    private final ItemService itemService;

    public ItemFormDto registerReview(Long itemId, String memberEmail, String contents){
        /*
        1. member
            - member exists
        2. item
            - item 구매
        3. save
         */
        Member member = memberService.findByEmail(memberEmail);
//        itemService.getItemDtl()

        return null;
    }
}
