package com.example.tnovel_backend.scheduler;

import com.example.tnovel_backend.repository.payment.SubscriptionPaymentRepository;
import com.example.tnovel_backend.repository.payment.entity.SubscriptionPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SubscriptionPaymentValidator {

    public final SubscriptionPaymentRepository subscriptionPaymentRepository;

    @Scheduled(cron = "0 0 19 * * *") // 매시 정각마다 실행

    @Transactional(readOnly = true)
    public void validatePaidAmount() {
        List<SubscriptionPayment> invalidPayments = subscriptionPaymentRepository
                .findInvalidPaidAmounts();

        if (invalidPayments.isEmpty()) {
            log.info("[Payment Validation] 이상 결제 없음");
            return;
        }

        for (SubscriptionPayment payment : invalidPayments) {
            log.warn("[Payment Validation] 결제 금액 불일치 발생! merchantUid={}, impUid={}, expected={}, paid={}",
                    payment.getMerchantUid(),
                    payment.getImpUid(),
                    payment.getAmountExpected(),
                    payment.getAmountPaid()
            );

        }
    }
}
