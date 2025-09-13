package com.example.tnovel_backend.controller.user;

import com.example.tnovel_backend.controller.user.dto.request.LocalLoginRequestDto;
import com.example.tnovel_backend.controller.user.dto.request.LocalSignUpRequestDto;
import com.example.tnovel_backend.controller.user.dto.response.LoginResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.UserSimpleResponseDto;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.UserErrorCode;
import com.example.tnovel_backend.repository.user.UserRepository;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.service.application.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "회원가입", description = "로컬 회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signupLocal(@Valid @RequestBody LocalSignUpRequestDto request) {
        SignUpResponseDto response = userService.signupLocal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로컬 로그인", description = "username 또는 phoneNumber와 password로 로그인합니다.\n" + "성공 시 JWT 토큰을 반환합니다.")
    @PostMapping("/login/local")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LocalLoginRequestDto request) {
        return ResponseEntity.ok(userService.loginLocal(request));
    }

    @Operation(
            summary = "카카오 로그인 안내",
            description = """
                    카카오 로그인은 Swagger에서 직접 테스트할 수 없습니다.
                    대신 브라우저에서 아래 URL로 접속하면 카카오 로그인 페이지로 이동합니다.
                    로그인 후에는 DB에 유저가 저장됩니다.
                    
                    [카카오 로그인 시작하기](http://localhost:8080/oauth2/authorization/kakao)
                    """
    )
    @GetMapping("/kakao-login-info")
    public ResponseEntity<String> kakaoLoginInfo() {
        return ResponseEntity.ok("브라우저에서 http://localhost:8080/oauth2/authorization/kakao 로 접속하세요.");
    }

    @Operation(summary = "유저 휴면 전환", description = "특정 유저 계정을 휴면 상태로 전환합니다.")
    @PatchMapping("/{id}/dormant")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserSimpleResponseDto> makeDormant(@Parameter(example = "1") @PathVariable Integer id, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if (!user.getId().equals(id)) {
            throw new UserException(UserErrorCode.FORBIDDEN); // 새 에러코드 정의하면 됨
        }
        return ResponseEntity.ok(userService.makeDormant(id));
    }

    @Operation(summary = "유저(어드민포함) 회원 탈퇴", description = "특정 유저 계정을 회원 탈퇴로 전환합니다.")
    @DeleteMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserSimpleResponseDto> withdraw(@Parameter(example = "1") @PathVariable Integer id, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if (!user.getId().equals(id)) {
            throw new UserException(UserErrorCode.FORBIDDEN); // 새 에러코드 정의하면 됨
        }
        return ResponseEntity.ok(userService.withdraw(id));
    }

}
