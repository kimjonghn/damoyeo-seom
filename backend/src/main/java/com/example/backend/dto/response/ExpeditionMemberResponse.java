package com.example.backend.dto.response;

import com.example.backend.domain.MemberRole;

import java.time.LocalDateTime;

public record ExpeditionMemberResponse(
        Long userId,
        String nickname,
        String profileImageUrl,
        MemberRole role,
        LocalDateTime joinedAt
) {
}