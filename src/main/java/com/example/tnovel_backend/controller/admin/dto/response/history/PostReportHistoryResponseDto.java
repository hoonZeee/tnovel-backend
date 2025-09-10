package com.example.tnovel_backend.controller.admin.dto.response.history;

import com.example.tnovel_backend.repository.post.entity.PostReportHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostReportHistoryResponseDto {
    private Integer id;
    private Integer reportId;
    private String reportUsername;
    private Integer postId;
    private String action;
    private LocalDateTime createdAt;

    public static PostReportHistoryResponseDto from(PostReportHistory history) {
        return new PostReportHistoryResponseDto(
                history.getId(),
                history.getReportId(),
                history.getReportUsername(),
                history.getPostId(),
                history.getAction(),
                history.getCreatedAt()
        );
    }
}
