package com.example.tnovel_backend.repository.payment.entity;

import com.example.tnovel_backend.repository.payment.entity.vo.PayStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String impUid; //아임포트 고유결제 id
    private String merchantUid; //결요청 id
    private BigDecimal amountExpected; //서버권위금액
    private BigDecimal amountPaid; //실결제금액

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    private LocalDateTime paidAt;

    private String failReason;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    public static SubscriptionPayment create(String impUid, String merchantUid, BigDecimal amountExpected, BigDecimal amountPaid, PayStatus payStatus, LocalDateTime paidAt, Subscription subscription) {
        return new SubscriptionPayment(
                null,
                impUid,
                merchantUid,
                amountExpected,
                amountPaid,
                PayStatus.PAID,
                paidAt,
                null,
                subscription
        );
    }

    public static SubscriptionPayment createFail(String merchantUid, BigDecimal amountExpected, String failReason, Subscription subscription) {
        return new SubscriptionPayment(
                null,
                null,
                merchantUid,
                amountExpected,
                null,
                PayStatus.FAILED,
                null,
                failReason,
                subscription
        );
    }

}
