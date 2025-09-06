package com.example.tnovel_backend.repository.payment.entity;

import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import com.example.tnovel_backend.repository.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private SubscribeStatus subscribeStatus;

    private LocalDateTime startedAt;
    private LocalDateTime canceledAt;
    private LocalDateTime nextBillingDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionPayment> subscriptionPayments = new ArrayList<>();


    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionStatusHistory> subscriptionStatusHistories = new ArrayList<>();


    public static Subscription create(User user) {
        return new Subscription(
                null,
                SubscribeStatus.ACTIVE,
                LocalDateTime.now(),
                null,
                LocalDateTime.now().plusMonths(1),
                user,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

}
