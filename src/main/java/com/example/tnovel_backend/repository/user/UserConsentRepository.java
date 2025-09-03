package com.example.tnovel_backend.repository.user;

import com.example.tnovel_backend.repository.user.entity.UserConsent;
import com.example.tnovel_backend.repository.user.entity.vo.ConsentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserConsentRepository extends JpaRepository<UserConsent, Integer> {
    List<UserConsent> findByConsentTypeAndAgreedAtBefore(ConsentType consentType, LocalDate agreedAtBefore);
}
