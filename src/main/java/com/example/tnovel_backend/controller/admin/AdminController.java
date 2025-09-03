package com.example.tnovel_backend.controller.admin;

import com.example.tnovel_backend.controller.admin.dto.request.AdminLoginRequestDto;
import com.example.tnovel_backend.controller.admin.dto.request.AdminSignUpRequestDto;
import com.example.tnovel_backend.controller.user.dto.response.LoginResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.UserSimpleResponseDto;
import com.example.tnovel_backend.service.application.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "어드민", description = "어드민 관련 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "어드민 회원가입", description = "어드민 계정 생성 API")
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signupAdmin(@RequestBody @Valid AdminSignUpRequestDto request) {
        SignUpResponseDto response = adminService.signupAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "어드민 로그인", description = "어드민 아이디 기반 로그인 API")
    @PostMapping("/login")
    public LoginResponseDto loginAdmin(@RequestBody @Valid AdminLoginRequestDto request) {
        return adminService.loginAdmin(request);
    }

    @Operation(summary = "유저 전체 조회", description = "어드민 accessToken을 기반으로 조회")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserSimpleResponseDto>> getAllUsers() {
        List<UserSimpleResponseDto> users = adminService.getAllUsersOrderByCreatedAtDesc();
        return ResponseEntity.ok(users);
    }

}
