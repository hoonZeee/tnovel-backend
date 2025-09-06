package com.example.tnovel_backend.controller.admin.dto.response;

import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionSimpleResponseDto {

    private Integer subscriptionId;
    private String username;
    private String name;
    private SubscribeStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime canceledAt;
    private LocalDateTime nextBillingDate;

    public static SubscriptionSimpleResponseDto from(Subscription s) {
        return new SubscriptionSimpleResponseDto(
                s.getId(),
                s.getUser().getUsername(),
                s.getUser().getName(),
                s.getSubscribeStatus(),
                s.getStartedAt(),
                s.getCanceledAt(),
                s.getNextBillingDate()
        );
    }
}
