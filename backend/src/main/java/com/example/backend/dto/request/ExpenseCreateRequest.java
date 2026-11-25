package com.example.backend.dto.request;

import com.example.backend.domain.ExpenseCalculationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExpenseCreateRequest(

        @NotBlank(message = "비용 항목명은 필수입니다.")
        String name,

        @NotNull(message = "금액은 필수입니다.")
        @DecimalMin(value = "0", inclusive = false)
        BigDecimal amount,

        @NotNull(message = "계산 방식은 필수입니다.")
        ExpenseCalculationType calculationType,

        @NotNull(message = "수량은 필수입니다.")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        Integer quantity

) {
}