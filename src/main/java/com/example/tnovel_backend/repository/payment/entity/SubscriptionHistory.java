package com.example.tnovel_backend.repository.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer subscriptionId;
    private String username;
    private String action;
    private LocalDateTime createdAt;

    public static SubscriptionHistory create(Integer subscriptionId, String username, String action) {
        return new SubscriptionHistory(
                null,
                subscriptionId,
                username,
                action,
                LocalDateTime.now()
        );
    }
}
