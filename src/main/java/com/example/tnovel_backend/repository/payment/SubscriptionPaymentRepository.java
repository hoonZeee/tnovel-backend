package com.example.tnovel_backend.repository.payment;

import com.example.tnovel_backend.repository.payment.entity.SubscriptionPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPayment, Integer> {
}
