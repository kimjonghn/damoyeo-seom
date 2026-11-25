package com.example.backend.repository;

import com.example.backend.entity.Expense;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//비용계산
public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    @EntityGraph(attributePaths = "createdBy")
    List<Expense>
    findAllByExpedition_ExpeditionIdOrderByCreatedAtAsc(
            Long expeditionId
    );
}