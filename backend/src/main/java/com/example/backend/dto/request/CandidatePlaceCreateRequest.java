package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CandidatePlaceCreateRequest(

        String kakaoPlaceId,

        @NotBlank(message = "장소명은 필수입니다.")
        @Size(max = 255)
        String placeName,

        String address,

        BigDecimal latitude,

        BigDecimal longitude,

        String memo
) {
}
