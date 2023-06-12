package com.shop.farmers.boundedContext.home.controller;

import com.shop.farmers.boundedContext.item.dto.ItemSearchDto;
import com.shop.farmers.boundedContext.item.dto.MainItemDto;
import com.shop.farmers.boundedContext.item.service.ItemService;
import com.shop.farmers.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        if(principal != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 유저의 인증정보를 가져와서 저장(GET)
            User user = (User) auth.getPrincipal(); // 유저의 정보를 저장
            SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(auth, user.getUsername())); // 새 정보를 가져와서 갱신(SET)
        }

        return "main";

    }

    // 권한 조회
    protected Authentication createNewAuthentication(Authentication currentAuth, String username) {
        UserDetails newPrincipal = memberService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), newPrincipal.getAuthorities());
        newAuth.setDetails(currentAuth.getDetails());
        return newAuth;
    }
}
