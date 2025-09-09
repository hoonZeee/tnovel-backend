package com.example.tnovel_backend.controller.post.dto.request;

import com.example.tnovel_backend.repository.post.entity.vo.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostReportRequestDto {

    @NotNull(message = "신고사유는 필수입니다.")
    private ReportReason reportReason;
}
