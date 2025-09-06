package com.example.tnovel_backend.repository.payment;

import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.payment.entity.SubscriptionPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPayment, Integer> {

    Optional<SubscriptionPayment> findTopBySubscriptionOrderByPaidAtDesc(Subscription subscription);

    @Query("SELECT p FROM SubscriptionPayment p " +
            "WHERE p.payStatus = 'PAID' " +
            "AND (p.amountPaid IS NULL OR p.amountPaid <> p.amountExpected)")
    List<SubscriptionPayment> findInvalidPaidAmounts();

}
