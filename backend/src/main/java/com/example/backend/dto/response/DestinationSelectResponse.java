package com.example.backend.dto.response;

public record DestinationSelectResponse(
        Long expeditionId,
        Long candidatePlaceId,
        String placeName
) {
}