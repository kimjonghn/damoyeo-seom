package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChecklistItemCreateRequest(

        @NotBlank(message = "준비물 이름은 필수입니다.")
        String name,

        Long assignedUserId

) {
}