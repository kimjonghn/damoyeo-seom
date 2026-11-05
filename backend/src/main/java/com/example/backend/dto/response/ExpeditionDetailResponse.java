package com.example.backend.dto.response;

import com.example.backend.domain.ExpeditionStatus;
import com.example.backend.domain.MemberRole;

import java.time.LocalDate;
//원정 상세 조회
public record ExpeditionDetailResponse(
        Long expeditionId,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String departureLocation,
        Integer expectedMemberCount,
        Integer vehicleCount,
        String inviteCode,
        ExpeditionStatus status,
        Long selectedCandidateId,
        MemberRole myRole
) {
}