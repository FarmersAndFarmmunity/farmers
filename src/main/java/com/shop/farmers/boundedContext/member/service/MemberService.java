package com.shop.farmers.boundedContext.member.service;

import com.shop.farmers.boundedContext.member.constant.Role;
import com.shop.farmers.boundedContext.member.dto.MemberSearchDto;
import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Transactional
    // 일반 회원가입
    public Member join(String email, String password) {
        return join("Farmers", email, password);
    }

    public Optional<Member> getMemberById(Long id){
        return memberRepository.findById(id);
    }

    // 소셜 로그인
    private Member join(String providerTypeCode, String username, String password)  throws UsernameNotFoundException {
        if(findByUsername(username).isPresent()){
            throw new UsernameNotFoundException(username);
        }

        Member member = Member
                .builder()
                .providerTypeCode(providerTypeCode)
                .username(username)
                .password(password)
                .role(Role.ADMIN)
                .build();

        return memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    // 소셜 로그인 시 실행되는 함수
    public Member whenSocialLogin(String providerTypeCode, String username) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent())
            return opMember.get();

        // 소셜 로그인를 통한 가입 시 비밀번호는 없다.
        return join(providerTypeCode, username, ""); // 최초 로그인 시 딱 한번 실행
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
  
    @Transactional(readOnly = true)
    public Page<Member> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable) {
        return memberRepository.getAdminMemberPage(memberSearchDto, pageable);
    }
}
