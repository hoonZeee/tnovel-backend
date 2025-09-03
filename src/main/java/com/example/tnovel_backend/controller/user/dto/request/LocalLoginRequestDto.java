package com.example.tnovel_backend.controller.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocalLoginRequestDto {

    @Schema(description = "아이디 (username 또는 phoneNumber 중 하나 필수)", example = "happypuppy")
    private String username;

    @Schema(description = "전화번호 (username 또는 phoneNumber 중 하나 필수)", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "비밀번호", example = "HappyPuppy123@")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

}
