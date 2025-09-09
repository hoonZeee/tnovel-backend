package com.example.tnovel_backend.controller.admin.dto.response;

import com.example.tnovel_backend.repository.post.entity.PostReport;
import com.example.tnovel_backend.repository.post.entity.vo.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminPostReportResponseDto {

    private Integer reportId;
    private String reporterUsername;
    private Integer postId;
    private String postTitle;
    private String postSnippet;
    private ReportReason reportReason;
    private LocalDateTime reportedAt;

    public static AdminPostReportResponseDto from(PostReport report) {
        return new AdminPostReportResponseDto(
                report.getId(),
                report.getUser().getUsername(),
                report.getPost().getId(),
                report.getPost().getTitle(),
                trimContent(report.getPost().getContent()),
                report.getReportReason(),
                report.getCreatedAt()
        );
    }

    private static String trimContent(String content) {
        return content == null ? "" : content.substring(0, Math.min(30, content.length()));
    }
}
