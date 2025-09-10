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
public class CommentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer commentId;
    private String username;
    private String action;
    private LocalDateTime createdAt;

    public static CommentHistory create(Integer commentId, String username, String action) {
        return new CommentHistory(
                null,
                commentId,
                username,
                action,
                LocalDateTime.now()
        );
    }
}
