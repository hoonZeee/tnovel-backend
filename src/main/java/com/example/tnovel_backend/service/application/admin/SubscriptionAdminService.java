package com.example.tnovel_backend.service.application.admin;

import com.example.tnovel_backend.controller.admin.dto.request.SubscriptionSearchRequest;
import com.example.tnovel_backend.controller.admin.dto.response.SubscriptionSimpleResponseDto;
import com.example.tnovel_backend.repository.payment.SubscriptionRepository;
import com.example.tnovel_backend.repository.payment.entity.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionAdminService {

    private final SubscriptionRepository subscriptionRepository;

    public Page<SubscriptionSimpleResponseDto> getAllSubscriptions(Pageable pageable) {
        Page<Subscription> page = subscriptionRepository.findAllOrderedByLatestActivity(pageable);
        return page.map(SubscriptionSimpleResponseDto::from);
    }

    public Page<SubscriptionSimpleResponseDto> searchSubscriptions(SubscriptionSearchRequest request, Pageable pageable) {
        return subscriptionRepository.searchSubscription(request, pageable)
                .map(SubscriptionSimpleResponseDto::from);
    }
}
