package com.example.tnovel_backend.controller.admin.dto.request;

import com.example.tnovel_backend.repository.payment.entity.vo.SubscribeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SubscriptionSearchRequest {
    @Schema(example = "happy")
    private String username;
    @Schema(example = "행복")
    private String name;
    @Schema(example = "2025-09-04")
    private LocalDate startedDate;
    private LocalDate canceledDate;
    @Schema(example = "ACTIVE")
    private SubscribeStatus subscribeStatus;
}
