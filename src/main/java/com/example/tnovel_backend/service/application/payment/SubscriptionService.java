package com.example.tnovel_backend.service.application.payment;

import com.example.tnovel_backend.controller.payment.dto.request.ConfirmSubscribeRequestDto;
import com.example.tnovel_backend.controller.payment.dto.response.CancelSubscribeResponseDto;
import com.example.tnovel_backend.controller.payment.dto.response.ConfirmSubscribeResponseDto;
import com.example.tnovel_backend.controller.payment.dto.response.PrepareSubscribeResponseDto;
import com.example.tnovel_backend.controller.payment.dto.response.VerifySubscribeResponseDto;
import com.example.tnovel_backend.exception.domain.BusinessException;
import com.example.tnovel_backend.exception.domain.SubscriptionException;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.SubscriptionErrorCode;
import com.example.tnovel_backend.exception.error.UserErrorCode;
import com.example.tnovel_backend.repository.payment.SubscriptionPaymentRepository;
import com.example.tnovel_backend.repository.payment.SubscriptionRepository;
import com.example.tnovel_backend.repository.payment.SubscriptionStatusHistoryRepository;
import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.payment.entity.SubscriptionPayment;
import com.example.tnovel_backend.repository.payment.entity.SubscriptionStatusHistory;
import com.example.tnovel_backend.repository.payment.entity.vo.PayStatus;
import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.service.domain.payment.PortOneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final PortOneService portOneService;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPaymentRepository subscriptionPaymentRepository;
    private final SubscriptionStatusHistoryRepository subscriptionStatusHistoryRepository;


    @Transactional
    public CancelSubscribeResponseDto cancelSubscribe(User user, String reason) {
        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        SubscriptionPayment payment = subscriptionPaymentRepository
                .findTopBySubscriptionOrderByPaidAtDesc(subscription)
                .orElseThrow(() -> new SubscriptionException(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND));

        //실제 결제 취소가 불가하기에 주석처리
        //portOneService.cancelPayment(payment.getImpUid(), reason);

        subscription.cancel();

        SubscriptionStatusHistory history = SubscriptionStatusHistory.create(
                SubscribeStatus.CANCELED, reason, subscription);
        subscriptionStatusHistoryRepository.save(history);

        return CancelSubscribeResponseDto.from(subscription, reason);
    }


    @Transactional //결제 성공 로직
    public ConfirmSubscribeResponseDto confirmSubscribe(ConfirmSubscribeRequestDto dto, User user) {

        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseGet(() -> {
                    //실제 결제가 불가능 함에 따라 DB검증을 위한 로직입니다.
                    Subscription created = Subscription.create(user);
                    return subscriptionRepository.save(created);
                });


        SubscriptionPayment payment = SubscriptionPayment.create(
                dto.getImpUid(),
                dto.getMerchantUid(),
                new BigDecimal("9900"),
                dto.getPaidAmount(),
                PayStatus.PAID,
                LocalDateTime.now(),
                subscription
        );
        subscriptionPaymentRepository.save(payment);

        SubscriptionStatusHistory history = SubscriptionStatusHistory.create(
                SubscribeStatus.ACTIVE,
                "결제 완료",
                subscription
        );
        subscriptionStatusHistoryRepository.save(history);

        return ConfirmSubscribeResponseDto.from(payment, subscription);
    }

    @Transactional // 결제 실패 로직
    public ConfirmSubscribeResponseDto confirmSubscribeFail(ConfirmSubscribeRequestDto dto, User user, String failReason) {

        Subscription subscription = subscriptionRepository.findByUser(user)
                .orElseGet(() -> {
                    // 실제 결제가 불가능 함에 따라 DB검증을 위한 로직입니다.
                    Subscription created = Subscription.create(user);
                    return subscriptionRepository.save(created);
                });

        SubscriptionPayment payment = SubscriptionPayment.createFail(
                dto.getMerchantUid(),
                new BigDecimal("9900"),
                failReason,
                subscription
        );
        subscriptionPaymentRepository.save(payment);


        SubscriptionStatusHistory history = SubscriptionStatusHistory.create(
                SubscribeStatus.CANCELED,
                failReason,
                subscription
        );
        subscriptionStatusHistoryRepository.save(history);

        subscription.fail();

        return ConfirmSubscribeResponseDto.from(payment, subscription);
    }


    public VerifySubscribeResponseDto verifySubscribe(String impUid, User user) {
        BigDecimal amount = portOneService.getPaymentAmount(impUid);

        return VerifySubscribeResponseDto.from(amount);
    }

    public PrepareSubscribeResponseDto prepareSubscribe(User user) {
        String merchantUid = generateMerchantUid();
        BigDecimal amount = new BigDecimal("9900");

        portOneService.preparePayment(merchantUid, amount);

        return new PrepareSubscribeResponseDto(
                merchantUid,
                amount,
                user.getUsername()
        );
    }

    private String generateMerchantUid() {
        return "subscr_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                "_" +
                UUID.randomUUID().toString().substring(0, 8);
    }
}
