package com.shop.farmers.boundedContext.member.repository;

import com.shop.farmers.boundedContext.member.dto.MemberSearchDto;
import com.shop.farmers.boundedContext.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable);
}
