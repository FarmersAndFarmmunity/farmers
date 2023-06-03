package com.shop.farmers.boundedContext.member.entity;

import com.shop.farmers.base.baseEntity.BaseEntity;
import com.shop.farmers.boundedContext.member.constant.Role;
import com.shop.farmers.boundedContext.member.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER); // 현재는 멤버의 롤이 기본적으로 ADMIN으로 설정되어 있다.
        return member;
    }
}