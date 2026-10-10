package com.example.backend.dto.response;

import com.example.backend.domain.ExpeditionStatus;

import java.time.LocalDate;

public record ExpeditionCreateResponse(

        Long expeditionId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String departureLocation,
        Integer expectedMemberCount,
        Integer vehicleCount,
        String inviteCode,
        ExpeditionStatus status

) {
}