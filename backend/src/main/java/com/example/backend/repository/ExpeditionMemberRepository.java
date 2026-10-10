package com.example.backend.repository;

import com.example.backend.entity.Expedition;
import com.example.backend.entity.ExpeditionMember;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpeditionMemberRepository
        extends JpaRepository<ExpeditionMember, Long> {

    //이미 해당 원정에 참가한 사람인지 검사
    boolean existsByExpeditionAndUser(
            Expedition expedition,
            User user
    );
}