package com.example.tnovel_backend.controller.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ConfirmSubscribeRequestDto {

    @Schema(description = "아임포트 결제 고유 ID", example = "imp_1234567890")
    private String impUid;

    @Schema(description = "주문 고유 ID (merchant_uid)", example = "subscr_20250906104857_a5018bb3")
    private String merchantUid;

    @Schema(description = "사용자가 실제 결제한 금액", example = "9900")
    private BigDecimal paidAmount;
}
