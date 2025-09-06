package com.example.tnovel_backend.repository.payment;

import com.example.tnovel_backend.repository.payment.entity.Subscription;
import com.example.tnovel_backend.repository.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Optional<Subscription> findByUser(User user);

    @Query("SELECT s FROM Subscription s WHERE DATE(s.nextBillingDate) = :today")
    List<Subscription> findByNextBillingDate(@Param("today") LocalDate today);
}
