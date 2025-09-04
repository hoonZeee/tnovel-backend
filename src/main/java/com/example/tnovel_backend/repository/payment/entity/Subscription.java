package com.example.tnovel_backend.repository.payment.entity;

import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import com.example.tnovel_backend.repository.user.entity.User;
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
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private SubscribeStatus subscribeStatus;

    private LocalDateTime startedAt;
    private LocalDateTime canceledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "subscription")
    private SubscriptionPayment subscriptionPayment;


    public static Subscription create(User user) {
        return new Subscription(
                null,
                SubscribeStatus.ACTIVE,
                LocalDateTime.now(),
                null,
                user,
                null
        );
    }

}
