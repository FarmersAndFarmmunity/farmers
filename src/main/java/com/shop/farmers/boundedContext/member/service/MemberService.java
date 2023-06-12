package com.shop.farmers.boundedContext.member.service;

import com.shop.farmers.boundedContext.member.constant.Role;
import com.shop.farmers.boundedContext.member.entity.Member;
import com.shop.farmers.boundedContext.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

}
