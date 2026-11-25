package com.example.backend.repository;

import com.example.backend.entity.ChecklistItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistItemRepository
        extends JpaRepository<ChecklistItem, Long> {

    @EntityGraph(attributePaths = {"createdBy", "assignedUser"})
    List<ChecklistItem>
    findAllByExpedition_ExpeditionIdOrderByCreatedAtAsc(
            Long expeditionId
    );

    Optional<ChecklistItem>
    findByChecklistItemIdAndExpedition_ExpeditionId(
            Long checklistItemId,
            Long expeditionId
    );
}