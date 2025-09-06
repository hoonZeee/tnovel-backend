package com.example.tnovel_backend.repository.payment;

import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Optional<Subscription> findByUser(User user);
}
