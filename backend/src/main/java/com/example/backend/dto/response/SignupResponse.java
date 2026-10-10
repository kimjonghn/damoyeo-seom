package com.example.backend.dto.response;

public record SignupResponse(
        Long userId,
        String email,
        String nickname
) {
}