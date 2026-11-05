package com.example.backend.repository;

import com.example.backend.entity.CandidatePlace;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidatePlaceRepository
        extends JpaRepository<CandidatePlace, Long> {
    //목록조회
    @EntityGraph(attributePaths = "createdBy")
    List<CandidatePlace>
    findAllByExpedition_ExpeditionIdOrderByCreatedAtAsc(
            Long expeditionId
    );

    Optional<CandidatePlace>
    findByCandidatePlaceIdAndExpedition_ExpeditionId(
            Long candidatePlaceId,
            Long expeditionId
    );
}