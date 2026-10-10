package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExpeditionJoinRequest(

        @NotBlank(message = "초대코드는 필수입니다.")
        @Size(max = 30, message = "올바르지 않은 초대코드입니다.")
        String inviteCode

) {
}