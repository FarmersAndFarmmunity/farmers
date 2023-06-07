package com.shop.farmers.boundedContext.order.controller;

import com.shop.farmers.boundedContext.order.dto.OrderDto;
import com.shop.farmers.boundedContext.order.dto.OrderHistDto;
import com.shop.farmers.boundedContext.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    // 자바 객체를 HTTP 요청의 body로 전달
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                                              BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) { // orderDto객체에 데이터 바인딩 시 에러 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(),
                    HttpStatus.BAD_REQUEST); // 에러 정보를 ResponseEntity 객체에 담아서 반환
        }

        String email = principal.getName(); // principal 객체에서 현재 로그인한 회원의 이메일 정보를 조회

        Long orderId;

        try {
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/review", "/review/{itemId}"})
    public String showReview(@PathVariable("itemId") Long itemId, Principal principal, Model model) {

        // 주문 번호를 통해 주문한 상품의 item_id에 접근해야 함


        return "item/itemDtl";
    }

}