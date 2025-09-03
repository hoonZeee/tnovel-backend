package com.example.tnovel_backend.controller.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequestDto {
    @Schema(description = "관리자 아이디", example = "admin01")
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;

    @Schema(description = "비밀번호", example = "IAmAdmin123@")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
