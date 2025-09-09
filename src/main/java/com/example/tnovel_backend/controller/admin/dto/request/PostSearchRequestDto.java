package com.example.tnovel_backend.controller.admin.dto.request;

import com.example.tnovel_backend.repository.post.entity.vo.VisibleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostSearchRequestDto {
    @Schema(example = "happy")
    private String username;
    @Schema(example = "2025-09-09")
    private LocalDate createdDate;
    @Schema(example = "VISIBLE")
    private VisibleStatus visibleStatus;
}
