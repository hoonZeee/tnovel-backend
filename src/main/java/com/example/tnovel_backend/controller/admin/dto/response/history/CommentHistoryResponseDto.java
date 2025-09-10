package com.example.tnovel_backend.controller.admin.dto.response.history;

import com.example.tnovel_backend.repository.post.entity.CommentHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentHistoryResponseDto {
    private Integer id;
    private Integer commentId;
    private String username;
    private String action;
    private LocalDateTime createdAt;

    public static CommentHistoryResponseDto from(CommentHistory history) {
        return new CommentHistoryResponseDto(
                history.getId(),
                history.getCommentId(),
                history.getUsername(),
                history.getAction(),
                history.getCreatedAt()
        );
    }
}
