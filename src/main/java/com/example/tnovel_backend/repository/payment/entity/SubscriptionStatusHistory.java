package com.example.tnovel_backend.repository.payment.entity;

import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private SubscribeStatus status;

    private LocalDateTime changedAt;

    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    public static SubscriptionStatusHistory create(SubscribeStatus status, String reason, Subscription subscription) {
        return new SubscriptionStatusHistory(
                null,
                status,
                LocalDateTime.now(),
                reason,
                subscription
        );
    }


}
