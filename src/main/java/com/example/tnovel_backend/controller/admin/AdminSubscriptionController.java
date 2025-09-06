package com.example.tnovel_backend.controller.admin;

import com.example.tnovel_backend.controller.admin.dto.request.SubscriptionSearchRequest;
import com.example.tnovel_backend.controller.admin.dto.response.SubscriptionSimpleResponseDto;
import com.example.tnovel_backend.service.application.admin.SubscriptionAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "어드민 구독", description = "어드민 구독 관련 API")
@RestController
@RequestMapping("/api/admin/subscribes")
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final SubscriptionAdminService subscriptionAdminService;

    @Operation(summary = "구독 통합 검색", description = "username, name, 시작일, 취소일, 구독 상태 등 조합 검색")
    @PostMapping("/subscription/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SubscriptionSimpleResponseDto>> searchSubscriptions(
            @RequestBody SubscriptionSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        Page<SubscriptionSimpleResponseDto> result = subscriptionAdminService.searchSubscriptions(request, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "전체 구독 목록 조회", description = "조건 없이 최신순으로 구독 전체 조회 (canceledAt > startedAt 기준)")
    @GetMapping("/subscriptions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SubscriptionSimpleResponseDto>> getAllSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SubscriptionSimpleResponseDto> result = subscriptionAdminService.getAllSubscriptions(pageable);
        return ResponseEntity.ok(result);
    }
}
