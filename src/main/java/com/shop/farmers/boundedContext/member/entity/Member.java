package com.shop.farmers.boundedContext.member.entity;

import com.shop.farmers.base.baseEntity.BaseEntity;
import com.shop.farmers.boundedContext.member.constant.Role;
import com.shop.farmers.boundedContext.member.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerTypeCode;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setUsername(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN); // 현재는 멤버의 롤이 기본적으로 ADMIN 으로 설정되어 있다.
        return member;
    }

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 멤버는 ADMIN 권한을 가진다.
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

        return grantedAuthorities;
    }
}