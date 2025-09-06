package com.example.tnovel_backend.controller.payment.dto.response;

import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.payment.entity.SubscriptionPayment;
import com.example.tnovel_backend.repository.payment.entity.vo.PayStatus;
import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ConfirmSubscribeResponseDto {
    private String impUid;
    private String merchantUid;
    private BigDecimal amountPaid;
    private LocalDateTime paidAt;

    private SubscribeStatus subscribeStatus;
    private LocalDateTime startedAt;
    private LocalDateTime nextBillingDate;

    private PayStatus payStatus;

    public static ConfirmSubscribeResponseDto from(SubscriptionPayment payment, Subscription subscription) {
        return new ConfirmSubscribeResponseDto(
                payment.getImpUid(),
                payment.getMerchantUid(),
                payment.getAmountPaid(),
                payment.getPaidAt(),
                subscription.getSubscribeStatus(),
                subscription.getStartedAt(),
                subscription.getNextBillingDate(),
                payment.getPayStatus()
        );
    }
}
