package com.example.backend.repository;

import com.example.backend.entity.ExpeditionMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpeditionMemberRepository
        extends JpaRepository<ExpeditionMember, Long> {
}