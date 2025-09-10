package com.example.tnovel_backend.service.application.admin;

import com.example.tnovel_backend.controller.admin.dto.request.SubscriptionSearchRequest;
import com.example.tnovel_backend.controller.admin.dto.response.history.SubscriptionHistoryResponseDto;
import com.example.tnovel_backend.controller.admin.dto.response.SubscriptionSimpleResponseDto;
import com.example.tnovel_backend.repository.payment.SubscriptionHistoryRepository;
import com.example.tnovel_backend.repository.payment.SubscriptionRepository;
import com.example.tnovel_backend.repository.payment.entity.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionAdminService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;

    public Page<SubscriptionSimpleResponseDto> getAllSubscriptions(Pageable pageable) {
        Page<Subscription> page = subscriptionRepository.findAllOrderedByLatestActivity(pageable);
        return page.map(SubscriptionSimpleResponseDto::from);
    }

    public Page<SubscriptionSimpleResponseDto> searchSubscriptions(SubscriptionSearchRequest request, Pageable pageable) {
        return subscriptionRepository.searchSubscription(request, pageable)
                .map(SubscriptionSimpleResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<SubscriptionHistoryResponseDto> getHistories(Pageable pageable) {
        return subscriptionHistoryRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(SubscriptionHistoryResponseDto::from);
    }
}
