package com.example.tnovel_backend.controller.admin.dto.response.history;

import com.example.tnovel_backend.repository.payment.entity.SubscriptionHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SubscriptionHistoryResponseDto {
    private Integer id;
    private Integer subscriptionId;
    private String username;
    private String action;
    private LocalDateTime createdAt;

    public static SubscriptionHistoryResponseDto from(SubscriptionHistory history) {
        return new SubscriptionHistoryResponseDto(
                history.getId(),
                history.getSubscriptionId(),
                history.getUsername(),
                history.getAction(),
                history.getCreatedAt()
        );
    }
}
