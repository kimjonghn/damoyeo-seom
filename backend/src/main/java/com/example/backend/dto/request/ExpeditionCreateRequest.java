package com.example.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ExpeditionCreateRequest(

        @NotBlank(message = "원정 제목은 필수입니다.")
        @Size(max = 100, message = "원정 제목은 100자 이하이어야 합니다.")
        String title,

        String description,

        @NotNull(message = "출발 날짜는 필수입니다.")
        LocalDate startDate,

        @NotNull(message = "복귀 날짜는 필수입니다.")
        LocalDate endDate,

        String departureLocation,

        @Min(value = 1, message = "예상 인원은 최소 1명 이상이어야 합니다.")
        Integer expectedMemberCount,

        @NotNull(message = "차량 수는 필수입니다.")
        @Min(value = 0, message = "차량 수는 0대 이상이어야 합니다.")
        Integer vehicleCount

) {
}