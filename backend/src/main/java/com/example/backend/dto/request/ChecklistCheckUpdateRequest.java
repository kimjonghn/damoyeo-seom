package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChecklistCheckUpdateRequest(

        @NotNull(message = "체크 상태는 필수입니다.")
        Boolean isChecked

) {
}