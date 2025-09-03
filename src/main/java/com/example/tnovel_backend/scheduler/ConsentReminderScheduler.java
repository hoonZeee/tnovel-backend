package com.example.tnovel_backend.scheduler;

import com.example.tnovel_backend.repository.user.UserConsentRepository;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.repository.user.entity.UserConsent;
import com.example.tnovel_backend.repository.user.entity.vo.ConsentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsentReminderScheduler {

    private final UserConsentRepository userConsentRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional(readOnly = true)
    public void checkPrivacyConsentRenewal() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);

        List<UserConsent> expiredConsents = userConsentRepository.findByConsentTypeAndAgreedAtBefore(
                ConsentType.PRIVACY,
                oneYearAgo
        );

        if(expiredConsents.isEmpty()) {
            log.info("[스케쥴러] 만료된 개인정보 동의 없음");
            return;
        }

        for (UserConsent consent : expiredConsents) {
            User user = consent.getUser();
            log.info("[스케쥴러] 재동의 필요 유저 : id={}, username={}, 동의일자={}",
                    user.getId(),user.getUsername(),consent.getAgreedAt());
        }
    }



}
