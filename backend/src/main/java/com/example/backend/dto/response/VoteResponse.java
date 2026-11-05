package com.example.backend.dto.response;

public record VoteResponse(
        Long voteId,
        Long expeditionId,
        Long candidatePlaceId,
        String placeName,
        Long userId,
        String nickname
) {
}