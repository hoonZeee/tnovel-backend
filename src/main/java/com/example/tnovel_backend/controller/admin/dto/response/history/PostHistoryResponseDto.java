package com.example.tnovel_backend.controller.admin.dto.response.history;

import com.example.tnovel_backend.repository.post.entity.PostHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostHistoryResponseDto {
    private Integer id;
    private Integer postId;
    private String username;
    private String action;
    private LocalDateTime createdAt;

    public static PostHistoryResponseDto from(PostHistory history) {
        return new PostHistoryResponseDto(
                history.getId(),
                history.getPostId(),
                history.getUsername(),
                history.getAction(),
                history.getCreatedAt()
        );
    }
}
