package com.example.tnovel_backend.controller.admin.dto.request;

import com.example.tnovel_backend.repository.user.entity.vo.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserTotalSearchRequest {

    @Schema(example = "happy")
    private String username;

    @Schema(example = "행복")
    private String name;

    @Schema(example = "2025-09-04")
    private LocalDate createdDate;

    @Schema(example = "ACTIVE")
    private Status status;

}
