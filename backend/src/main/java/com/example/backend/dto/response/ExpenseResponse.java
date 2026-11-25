package com.example.backend.dto.response;

import com.example.backend.domain.ExpenseCalculationType;

import java.math.BigDecimal;

public record ExpenseResponse(
        Long expenseId,
        String name,
        BigDecimal amount,
        ExpenseCalculationType calculationType,
        Integer quantity,
        BigDecimal totalAmount,
        Long createdByUserId,
        String createdByNickname
) {
}