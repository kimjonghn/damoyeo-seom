package com.example.backend.dto.response;

import java.math.BigDecimal;

public record FishingSpotCreateResponse(

        Long fishingSpotId,
        Long candidatePlaceId,
        String candidatePlaceName,

        String name,

        BigDecimal latitude,
        BigDecimal longitude,

        BigDecimal depthMin,
        BigDecimal depthMax,

        String targetSpecies,
        String fishingMethods,
        String memo,

        Long createdByUserId,
        String createdByNickname

) {
}