package com.shop.farmers.boundedContext.payment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.shop.farmers.boundedContext.order.entity.Order;
import com.shop.farmers.boundedContext.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}/success")
    public String confirmPayment(
            @PathVariable Long id,
            String paymentKey,
            String orderId,
            Long amount,
            Model model
    ) throws Exception {

        Long parsedOrderId = Long.parseLong(orderId.split("__")[1]);

        Order order = paymentService.verifyRequest(id, parsedOrderId, amount);
        ResponseEntity<JsonNode> responseEntity = paymentService.requestPayment(paymentKey, orderId, amount);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            paymentService.payDone(order);
            return "redirect:/order/%d?msg=%s".formatted(order.getId(), URLEncoder.encode("결제가 완료되었습니다.", StandardCharsets.UTF_8));
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());

            return "order/orderFail";
        }
    }

    @GetMapping("/{id}/fail")
    public String failPayment(String message, String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/orderFail";
    }
}
