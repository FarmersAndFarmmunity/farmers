package com.shop.farmers.boundedContext.member.service;

import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
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

//    @Override
//    public UserDetails loadUserByUsername(String providerTypeCode, String email) throws UsernameNotFoundException {
//        Member member = memberRepository.findByEmail(email);
//
//        if(member == null){
//            throw new UsernameNotFoundException(email);
//        }
//
//        return User.builder()
//                //.providerTypeCode(member.getProviderTypeCode())
//                .username(member.getEmail())
//                .password(member.getPassword())
//                .roles(member.getRole().toString())
//                .build();
//    }

    @Transactional
    // 일반 회원가입
    public Member join(String email, String password) {
        return join("Farmers", email, password);
    }

    private Member join(String providerTypeCode, String email, String password) {
        Member findMember = memberRepository.findByEmail(email);

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        //소셜 로그인 시, 비밀번호가 없다.
        //if (StringUtils.hasText(password)) password = passwordEncoder.encode(password);

        Member member = Member
                .builder()
                .providerTypeCode(providerTypeCode)
                .username(email)
                .password(password)
                .build();

        return memberRepository.save(member);
    }

    // 소셜 로그인 시 실행되는 함수
    @Transactional
    public Member whenSocialLogin(String providerTypeCode, String email) {
//        Member opMember = memberRepository.findByEmail(email);
//
//        if (opMember != null)
//            return opMember.get();

        // 소셜 로그인를 통한 가입 시 비밀번호는 없다.
        return join(providerTypeCode, email, ""); // 최초 로그인 시 딱 한번 실행
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
