package com.example.backend.dto.response;

public record LoginResponse(
        Long userId,
        String email,
        String nickname,
        String accessToken
) {
}
