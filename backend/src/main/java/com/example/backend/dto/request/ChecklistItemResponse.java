package com.example.backend.dto.response;

import java.time.LocalDateTime;

public record ChecklistItemResponse(
        Long checklistItemId,
        String name,
        Boolean isChecked,
        LocalDateTime checkedAt,
        Long assignedUserId,
        String assignedUserNickname,
        Long createdByUserId,
        String createdByNickname
) {
}