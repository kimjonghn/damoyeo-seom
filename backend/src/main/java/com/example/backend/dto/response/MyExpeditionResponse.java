package com.example.backend.dto.response;

import com.example.backend.domain.ExpeditionStatus;
import com.example.backend.domain.MemberRole;

import java.time.LocalDate;
//내가 참가중인 원정 목록 조회
public record MyExpeditionResponse(
        Long expeditionId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String departureLocation,
        ExpeditionStatus status,
        MemberRole role
) {
}