package com.example.backend.repository;

import com.example.backend.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository
        extends JpaRepository<Vote, Long> {

    //투표전 해당 원정의 후보지인지 검사
    Optional<Vote>
    findByExpedition_ExpeditionIdAndUser_UserId(
            Long expeditionId,
            Long userId
    );

    //투표 결과 조회
    List<Vote>
    findAllByExpedition_ExpeditionId(
            Long expeditionId
    );
}