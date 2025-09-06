package com.example.tnovel_backend.controller.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PrepareSubscribeResponseDto {
    private String merchantUid;
    private BigDecimal amount;
    private String customerName;
}
