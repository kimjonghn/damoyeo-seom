package com.example.backend.dto.response;

import com.example.backend.domain.MemberRole;

public record ExpeditionJoinResponse(

        Long expeditionId,
        String title,
        MemberRole role

) {
}