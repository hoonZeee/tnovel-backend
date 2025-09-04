package com.example.tnovel_backend.controller.admin;

import com.example.tnovel_backend.controller.admin.dto.request.AdminLoginRequestDto;
import com.example.tnovel_backend.controller.admin.dto.request.AdminSignUpRequestDto;
import com.example.tnovel_backend.controller.admin.dto.request.UpdateUserStateRequestDto;
import com.example.tnovel_backend.controller.admin.dto.request.UserTotalSearchRequest;
import com.example.tnovel_backend.controller.user.dto.response.LoginResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.UserSimpleResponseDto;
import com.example.tnovel_backend.repository.user.entity.vo.Status;
import com.example.tnovel_backend.service.application.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @Operation(summary = "유저 전체 조회 (페이지네이션)", description = "어드민 accessToken을 기반으로 전체 유저를 최신순 조회")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserSimpleResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserSimpleResponseDto> users = adminService.getAllUsersOrderByCreatedAtDesc(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "유저 아이디기반 조회 (페이지네이션)", description = "username에 keyword가 포함된 유저를 최신순 조회")
    @GetMapping("/users/search/username")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserSimpleResponseDto>> searchByUsername(
            @Parameter(example = "happy") @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserSimpleResponseDto> created = adminService.searchByUsername(keyword, pageable);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "유저 닉네임기반 조회 (페이지네이션)", description = "name에 keyword가 포함된 유저를 최신순 조회")
    @GetMapping("/users/search/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserSimpleResponseDto>> searchByName(
            @Parameter(example = "행복한") @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserSimpleResponseDto> created = adminService.searchByName(keyword, pageable);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "유저 생성날짜기반 조회 (페이지네이션)", description = "2025-09-04처럼 입력된 날짜에 생성된 유저를 조회")
    @GetMapping("/users/search/created-date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserSimpleResponseDto>> searchByCreatedDate(
            @Parameter(example = "2025-09-04") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserSimpleResponseDto> created = adminService.searchByCreatedAt(date, pageable);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "회원 상태 기반 조회 (페이지네이션)", description = "ACTIVE, DORMANT, BANNED 등 상태에 따라 유저를 조회")
    @GetMapping("/users/search/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserSimpleResponseDto>> searchByStatus(
            @Parameter(example = "ACTIVE") @RequestParam Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserSimpleResponseDto> created = adminService.searchByStatus(status, pageable);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "유저 벤 하기", description = "조회 시 해당 유저를 벤 하기 위한 API.")
    @PatchMapping("/users/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserSimpleResponseDto> updateUserStatus(
            @PathVariable Integer userId,
            @RequestBody @Valid UpdateUserStateRequestDto request
    ) {
        UserSimpleResponseDto updated = adminService.updateUserStatus(userId, request.getStatus());
        return ResponseEntity.ok(updated);
    }


    @Operation(summary = "유저 통합 검색", description = "username, name, createdDate, status 등 조합 검색")
    @PostMapping("/user/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserSimpleResponseDto>> searchUsers(
            @RequestBody UserTotalSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<UserSimpleResponseDto> result = adminService.searchUsers(request, pageable);
        return ResponseEntity.ok(result);
    }


}
