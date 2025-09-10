package com.example.tnovel_backend.repository.payment;

import com.example.tnovel_backend.repository.payment.entity.SubscriptionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Integer> {

    Page<SubscriptionHistory> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
