package com.example.backend.controller;

import com.example.backend.dto.request.ExpenseCreateRequest;
import com.example.backend.dto.response.ExpenseCreateResponse;
import com.example.backend.dto.response.ExpenseSummaryResponse;
import com.example.backend.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expeditions/{expeditionId}/expenses")
@RequiredArgsConstructor
public class ExpenseController {
//비용계산
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseCreateResponse> createExpense(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId,
            @Valid @RequestBody ExpenseCreateRequest request
    ) {

        ExpenseCreateResponse response =
                expenseService.createExpense(
                        userId,
                        expeditionId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ExpenseSummaryResponse> getExpenses(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long expeditionId
    ) {

        ExpenseSummaryResponse response =
                expenseService.getExpenses(
                        userId,
                        expeditionId
                );

        return ResponseEntity.ok(response);
    }
}