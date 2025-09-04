package com.example.tnovel_backend.repository.post.entity;

import com.example.tnovel_backend.repository.post.entity.vo.VisibleStatus;
import com.example.tnovel_backend.repository.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String content;
    private Integer likeCount;

    @Enumerated(EnumType.STRING)
    private VisibleStatus visibleStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<PostMedia> postMedias = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<PostReport> postReports = new ArrayList<>();

    public static Post create(String title, String content, User user) {
        return new Post(
                null,
                title,
                content,
                0,
                VisibleStatus.VISIBLE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                user,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()

        );
    }

}
