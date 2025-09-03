package com.example.tnovel_backend.controller.user;

import com.example.tnovel_backend.controller.user.dto.request.LocalSignUpRequestDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.service.application.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Tag(name = "User", description = "유저 관련 API")
    @Operation(summary = "회원가입", description = "로컬 회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signupLocal(
            @Valid @RequestBody LocalSignUpRequestDto request) {
        SignUpResponseDto response = userService.signupLocal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }





}
