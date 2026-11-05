package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record VoteRequest(

        @NotNull(message = "후보지 선택은 필수입니다.")
        Long candidatePlaceId

) {
}