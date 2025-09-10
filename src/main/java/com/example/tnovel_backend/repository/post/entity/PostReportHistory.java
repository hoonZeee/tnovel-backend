package com.example.tnovel_backend.repository.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer reportId;
    private String reportUsername;
    private Integer postId;
    private String action;
    private LocalDateTime createdAt;

    public static PostReportHistory create(Integer reportId, String reportUsername, Integer postId, String action) {
        return new PostReportHistory(
                null,
                reportId,
                reportUsername,
                postId,
                action,
                LocalDateTime.now()
        );
    }
}
