package com.example.backend.dto.response;

public record ChecklistItemCreateResponse(
        Long checklistItemId,
        Long expeditionId,
        String name,
        Boolean isChecked,
        Long assignedUserId,
        String assignedUserNickname,
        Long createdByUserId,
        String createdByNickname
) {
}