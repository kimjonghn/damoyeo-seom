package com.example.backend.dto.response;

import java.math.BigDecimal;

public record CandidatePlaceResponse(
        Long candidatePlaceId,
        String placeName,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String memo,
        Long createdByUserId,
        String createdByNickname
) {
}