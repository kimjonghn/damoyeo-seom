package com.example.backend.dto.response;

import java.math.BigDecimal;

public record CandidatePlaceCreateResponse(
        Long candidatePlaceId,
        Long expeditionId,
        String placeName,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String memo,
        Long createdByUserId,
        String createdByNickname
) {
}