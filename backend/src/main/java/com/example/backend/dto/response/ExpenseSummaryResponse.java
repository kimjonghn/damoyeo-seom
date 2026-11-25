package com.example.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseSummaryResponse(
        Long expeditionId,
        List<ExpenseResponse> expenses,
        BigDecimal totalAmount,
        long participantCount,
        BigDecimal perPersonAmount
) {
}