package com.example.backend.repository;

import com.example.backend.entity.Expedition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpeditionRepository
        extends JpaRepository<Expedition, Long> {

    boolean existsByInviteCode(String inviteCode);
}