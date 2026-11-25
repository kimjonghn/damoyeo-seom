package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record FishingSpotCreateRequest(

        @NotBlank(message = "포인트 이름은 필수입니다.")
        String name,

        BigDecimal latitude,

        BigDecimal longitude,

        BigDecimal depthMin,

        BigDecimal depthMax,

        String targetSpecies,

        String fishingMethods,

        String memo

) {
}