package com.example.tnovel_backend.controller.payment.dto.response;

import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CancelSubscribeResponseDto {

    private Integer subscriptionId;
    private SubscribeStatus subscribeStatus;
    private LocalDateTime startedAt;
    private LocalDateTime canceledAt;
    private LocalDateTime nextBillingDate;
    private String reason;

    public static CancelSubscribeResponseDto from(Subscription subscription, String reason) {
        return new CancelSubscribeResponseDto(
                subscription.getId(),
                subscription.getSubscribeStatus(),
                subscription.getStartedAt(),
                subscription.getCanceledAt(),
                subscription.getNextBillingDate(),
                reason
        );
    }
}
