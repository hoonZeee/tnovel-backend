package com.example.tnovel_backend.repository.payment.entity;

import com.example.tnovel_backend.repository.payment.entity.vo.PayStatus;
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
public class SubscriptionPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String impUid; //아임포트 고유결제 id
    private String merchantUid; //결요청 id
    private Double amountExpected; //서버권위금액
    private Double amountPaid; //실결제금액

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    private LocalDateTime paidAt;

    private String failureCode;
    private String failureMessage;
    
    @OneToOne
    @JoinColumn(name = "subscription_id", unique = true)
    private Subscription subscription;

}
