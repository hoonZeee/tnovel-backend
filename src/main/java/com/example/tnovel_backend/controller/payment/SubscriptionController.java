package com.example.tnovel_backend.controller.payment;

import com.example.tnovel_backend.controller.payment.dto.request.ConfirmSubscribeRequestDto;
import com.example.tnovel_backend.controller.payment.dto.response.CancelSubscribeResponseDto;
import com.example.tnovel_backend.controller.payment.dto.response.ConfirmSubscribeResponseDto;
import com.example.tnovel_backend.controller.payment.dto.response.PrepareSubscribeResponseDto;
import com.example.tnovel_backend.controller.payment.dto.response.VerifySubscribeResponseDto;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.UserErrorCode;
import com.example.tnovel_backend.repository.user.UserRepository;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.service.application.payment.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@Tag(name = "포트원결제", description = "구독 관련 API")
@RestController
@RequestMapping("/api/subscribe")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;

    @Operation(summary = "결제 사전 등록", description = "포트원 결제 사전등록")
    @PostMapping("/prepare")
    public ResponseEntity<PrepareSubscribeResponseDto> prepare(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        PrepareSubscribeResponseDto response = subscriptionService.prepareSubscribe(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결제 검증", description = "포트원 결제 금액 검증")
    @PostMapping("/verify")
    public ResponseEntity<VerifySubscribeResponseDto> verify(
            @RequestParam("imp_uid") String impUid,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        VerifySubscribeResponseDto response = subscriptionService.verifySubscribe(impUid, user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결제 확정", description = "프론트에서 로직을 묶어 검증해야하지만, 현재 실 결제가 불가능해여 verify 호출이 안되는점을 고려해 결제가 성공했다고 가정하고 DB저장용 로직으로 서비스를 수정하였습니다.")
    @PostMapping("/confirm")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ConfirmSubscribeResponseDto> confirmSubscribe(
            @RequestBody ConfirmSubscribeRequestDto dto,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        ConfirmSubscribeResponseDto response = subscriptionService.confirmSubscribe(dto, user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결제 실패 확정", description = "결제 실패 내용을 서버에 반영하여 저장합니다.")
    @PostMapping("/confirm/fail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ConfirmSubscribeResponseDto> confirmSubscribeFail(
            @RequestBody ConfirmSubscribeRequestDto dto,
            @RequestParam("reason") String reason,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        ConfirmSubscribeResponseDto response = subscriptionService.confirmSubscribeFail(dto, user, reason);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "구독 취소", description = "현재 사용자의 구독을 취소합니다.")
    @PostMapping("/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CancelSubscribeResponseDto> cancelSubscribe(
            @RequestParam(required = false, defaultValue = "사용자 요청") String reason,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        CancelSubscribeResponseDto response = subscriptionService.cancelSubscribe(user, reason);
        return ResponseEntity.ok(response);
    }
}
