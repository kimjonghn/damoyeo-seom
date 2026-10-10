package com.example.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])\\S+$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함하고 공백을 사용할 수 없습니다."
        )
        String password,

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        @Size(max = 50, message = "닉네임은 50자 이하이어야 합니다.")
        String nickname

) {
}