package com.example.tnovel_backend.controller.post.dto.response;

import com.example.tnovel_backend.repository.post.entity.PostReport;
import com.example.tnovel_backend.repository.post.entity.vo.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostReportResponseDto {

    private Integer id;
    private ReportReason reportReason;
    private LocalDateTime createdAt;
    private String username;
    private Integer postId;

    public static PostReportResponseDto from(PostReport report) {
        return new PostReportResponseDto(
                report.getId(),
                report.getReportReason(),
                report.getCreatedAt(),
                report.getUser().getUsername(),
                report.getPost().getId()
        );
    }
}
