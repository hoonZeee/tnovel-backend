package com.example.tnovel_backend.repository.post.entity;

import com.example.tnovel_backend.repository.post.entity.vo.MediaType;
import com.example.tnovel_backend.repository.post.entity.vo.VisibleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    private String imageUrl;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static PostMedia create(String imageUrl, MediaType type, Post post) {
        return new PostMedia(
                null,
                type,
                imageUrl,
                LocalDateTime.now(),
                post
        );
    }
}
