package com.example.tnovel_backend.repository.user.entity;

import com.example.tnovel_backend.repository.user.entity.vo.ConsentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserConsent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ConsentType consentType;

    private LocalDate agreedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static UserConsent create(User user, ConsentType type) {
        return new UserConsent(
                null,
                type,
                LocalDate.now(),
                user
        );
    }

}
