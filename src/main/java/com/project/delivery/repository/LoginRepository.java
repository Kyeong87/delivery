package com.project.delivery.repository;

import com.project.delivery.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Member, Long> {

    Member findById(String id);

}
