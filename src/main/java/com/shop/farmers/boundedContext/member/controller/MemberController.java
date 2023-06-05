package com.shop.farmers.boundedContext.member.controller;

import com.shop.farmers.boundedContext.item.dto.ItemFormDto;
import com.shop.farmers.boundedContext.item.dto.ItemSearchDto;
import com.shop.farmers.boundedContext.item.entity.Item;
import com.shop.farmers.boundedContext.member.dto.MemberFormDto;
import com.shop.farmers.boundedContext.member.dto.MemberSearchDto;
import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


//@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/members/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/members/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/members/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/members/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "/member/memberLoginForm";
    }

    // 멤버 관리 기능

    @GetMapping(value = {"/admin/member", "/admin/member/{page}"})
    public String memberList(MemberSearchDto memberSearchDto, @PathVariable("page") Optional<Integer> page, Model model) throws Exception {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 3);

        Page<Member> members = memberService.getAdminMemberPage(memberSearchDto, pageable);
        model.addAttribute("members", members);
        model.addAttribute("memberSearchDto", memberSearchDto);
        model.addAttribute("maxPage", 5);

        return "member/memberList";
    }
}
