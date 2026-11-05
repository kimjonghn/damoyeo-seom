package com.example.backend.repository;

import com.example.backend.entity.Expedition;
import com.example.backend.entity.ExpeditionMember;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpeditionMemberRepository
        extends JpaRepository<ExpeditionMember, Long> {

    //이미 해당 원정에 참가한 사람인지 검사
    boolean existsByExpeditionAndUser(
            Expedition expedition,
            User user
    );
    //로그인한 사람이 원정 참가자가 맞는지
    boolean existsByExpedition_ExpeditionIdAndUser_UserId(
            Long expeditionId,
            Long userId
    );
    //실제 참가자 목록을 가져옴
    @EntityGraph(attributePaths = "user")
    List<ExpeditionMember>
    findAllByExpedition_ExpeditionIdOrderByJoinedAtAsc(
            Long expeditionId
    );

    //내가 참가중인 원정 목록 조회
    @EntityGraph(attributePaths = "expedition")
    List<ExpeditionMember>
    findAllByUser_UserIdOrderByJoinedAtDesc(Long userId);

    //원정 상세 조회
    Optional<ExpeditionMember>
    findByExpedition_ExpeditionIdAndUser_UserId(
            Long expeditionId,
            Long userId
    );
}