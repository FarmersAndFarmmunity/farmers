package com.shop.farmers.boundedContext.member.repository;

import com.shop.farmers.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Member findByEmail(String email);

}
