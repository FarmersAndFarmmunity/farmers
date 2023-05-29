package com.shop.farmers.repository;

<<<<<<< HEAD
import com.shop.farmers.constant.Role;
=======
>>>>>>> 6dff7bdf5143e309578640d2b0ea7c4477ad3b7a
import com.shop.farmers.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
}
