package com.example.tnovel_backend.repository.payment;

import com.example.tnovel_backend.controller.admin.dto.request.SubscriptionSearchRequest;
import com.example.tnovel_backend.repository.payment.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionRepositoryCustom {
    Page<Subscription> searchSubscription(SubscriptionSearchRequest request, Pageable pageable);

    Page<Subscription> findAllOrderedByLatestActivity(Pageable pageable);
}
