package com.example.backend.dto.response;

public record VoteResultResponse(
        Long candidatePlaceId,
        String placeName,
        long voteCount,
        boolean votedByMe
) {
}