package com.example.tnovel_backend.controller.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignUpRequestDto {
    @Schema(description = "전화번호", example = "01033334444")
    @NotBlank(message = "전화번호 입력 필수")
    private String phoneNumber;


    @Schema(description = "비밀번호", example = "IAmAdmin123@")
    @NotBlank(message = "비밀번호 입력 필수")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;


    @Schema(description = "아이디", example = "admin01")
    @NotBlank(message = "아이디 입력 필수")
    @Size(min = 4, max = 16, message = "아이디는 4~16자 사이여야 합니다.")
    private String username;


    @Schema(description = "닉네임", example = "관리자01")
    @NotBlank(message = "닉네임 입력 필수")
    @Size(max = 20, message = "닉네임은 20자 이하여야 합니다.")
    private String name;

}
