package com.example.tnovel_backend.controller.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class VerifySubscribeResponseDto {
    private boolean success;
    private BigDecimal amount;
    private String message;
    
    public static VerifySubscribeResponseDto from(BigDecimal amount) {
        return new VerifySubscribeResponseDto(
                true,
                amount,
                "결제 검증 성공");
    }
}
