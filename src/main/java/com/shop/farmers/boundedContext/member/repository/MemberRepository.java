package com.shop.farmers.boundedContext.member.repository;

import com.shop.farmers.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Member findByEmail(String email);
    Optional<Member> findByUsername(String username);

    Optional<Member> findById(Long id);
}
