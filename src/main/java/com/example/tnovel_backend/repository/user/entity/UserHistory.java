package com.example.tnovel_backend.repository.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String username;
    private String action;
    private LocalDateTime createdAt;

    public static UserHistory create(Integer userId, String username, String action) {
        return new UserHistory(
                null,
                userId,
                username,
                action,
                LocalDateTime.now()
        );
    }
}
