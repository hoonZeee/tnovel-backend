package com.example.tnovel_backend.scheduler;

import com.example.tnovel_backend.repository.payment.SubscriptionPaymentRepository;
import com.example.tnovel_backend.repository.payment.SubscriptionRepository;
import com.example.tnovel_backend.repository.payment.SubscriptionStatusHistoryRepository;
import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.payment.entity.SubscriptionPayment;
import com.example.tnovel_backend.repository.payment.entity.SubscriptionStatusHistory;
import com.example.tnovel_backend.repository.payment.entity.vo.PayStatus;
import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class SubscriptionBillingScheduler {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPaymentRepository subscriptionPaymentRepository;
    private final SubscriptionStatusHistoryRepository subscriptionStatusHistoryRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void executeScheduledBilling() {
        LocalDate today = LocalDate.now();
        List<Subscription> subscriptions = subscriptionRepository.findByNextBillingDate(today);

        for (Subscription subscription : subscriptions) {
            try {
                String merchantUid = "subscr_" + today + "_" + UUID.randomUUID();

                // 실제 결제가 불가능하여 일단 생성 메서드로 대체합니다.
                SubscriptionPayment payment = SubscriptionPayment.create(
                        "imp_dummy_" + UUID.randomUUID(),
                        merchantUid,
                        new BigDecimal("9900"),
                        new BigDecimal("9900"),
                        PayStatus.PAID,
                        LocalDateTime.now(),
                        subscription
                );
                subscriptionPaymentRepository.save(payment);

                // 다음 결제일 갱신 (한 달 뒤)
                subscription.postponeNextBillingDate();

                SubscriptionStatusHistory history = SubscriptionStatusHistory.create(
                        SubscribeStatus.ACTIVE,
                        "정기 결제 성공",
                        subscription
                );
                subscriptionStatusHistoryRepository.save(history);

                log.info(" [{}] 정기결제 완료", subscription.getUser().getUsername());
            } catch (Exception e) {
                log.error(" [{}] 정기결제 실패: {}", subscription.getUser().getUsername(), e.getMessage());
            }
        }
    }
}
