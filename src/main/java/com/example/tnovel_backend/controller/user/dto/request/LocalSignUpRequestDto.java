package com.example.tnovel_backend.controller.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocalSignUpRequestDto {

    @Schema(description = "전화번호", example = "01012345678")
    @NotBlank(message = "전화번호 입력 필수")
    private String phoneNumber;

    @Schema(description = "비밀번호", example = "HappyPuppy123@")
    @NotBlank(message = "비밀번호 입력 필수")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @Schema(description = "아이디", example = "happypuppy")
    @NotBlank(message = "유저아아디 입력 필수")
    @Size(min = 4, max = 16, message = "아이디는 4~16자 사이여야 합니다.")
    private String username;

    @Schema(description = "닉네임", example = "행복한강아지")
    @NotBlank(message = "닉네임 입력 필수")
    @Size(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    private String name;

    @Schema(description = "생년월일 (yyyy-MM-dd)", example = "2000-05-28")
    @NotNull(message = "생년월일은 필수 입력값입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;
}
