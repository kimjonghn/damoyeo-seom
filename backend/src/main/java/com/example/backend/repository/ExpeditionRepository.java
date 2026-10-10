package com.example.backend.repository;

import com.example.backend.entity.Expedition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpeditionRepository
        extends JpaRepository<Expedition, Long> {

    boolean existsByInviteCode(String inviteCode);

    //기존에 초대코드 중복검사
    Optional<Expedition> findByInviteCode(String inviteCode);
}