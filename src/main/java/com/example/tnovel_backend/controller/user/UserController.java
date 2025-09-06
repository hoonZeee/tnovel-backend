package com.example.tnovel_backend.controller.user;

import com.example.tnovel_backend.controller.user.dto.request.LocalLoginRequestDto;
import com.example.tnovel_backend.controller.user.dto.request.LocalSignUpRequestDto;
import com.example.tnovel_backend.controller.user.dto.response.LoginResponseDto;
import com.example.tnovel_backend.controller.user.dto.response.SignUpResponseDto;
import com.example.tnovel_backend.service.application.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
                    
                    카카오 로그인 시작하기](http://localhost:8080/oauth2/authorization/kakao)
                    """
    )
    @GetMapping("/kakao-login-info")
    public ResponseEntity<String> kakaoLoginInfo() {
        return ResponseEntity.ok("브라우저에서 http://localhost:8080/oauth2/authorization/kakao 로 접속하세요.");
    }


}
