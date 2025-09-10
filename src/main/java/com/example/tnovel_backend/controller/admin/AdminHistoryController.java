package com.example.tnovel_backend.controller.admin;

import com.example.tnovel_backend.controller.admin.dto.response.history.*;
import com.example.tnovel_backend.service.application.admin.PostAdminService;
import com.example.tnovel_backend.service.application.admin.UserAdminService;
import com.example.tnovel_backend.service.application.admin.SubscriptionAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "어드민 히스토리", description = "회원 관련 CRUD 히스토리 조회")
@RestController
@RequestMapping("/api/admin/histories")
@RequiredArgsConstructor
public class AdminHistoryController {
    private final UserAdminService userAdminService;
    private final SubscriptionAdminService subscriptionAdminService;
    private final PostAdminService postAdminService;


    @Operation(summary = "회원 히스토리 조회", description = "조건 없이 날짜순으로 회원 이력을 페이징 조회")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserHistoryResponseDto>> getUserHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserHistoryResponseDto> result = userAdminService.getUserHistories(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "구독 히스토리 조회", description = "조건 없이 최신순으로 구독 이력을 페이징 조회")
    @GetMapping("/subscriptions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SubscriptionHistoryResponseDto>> getHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(subscriptionAdminService.getHistories(pageable));
    }

    @Operation(summary = "게시물 히스토리 조회", description = "조건 없이 최신순으로 게시물 이력을 페이징 조회")
    @GetMapping("/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PostHistoryResponseDto>> getPostHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postAdminService.getPostHistories(pageable));
    }

    @Operation(summary = "댓글 히스토리 조회", description = "조건 없이 최신순으로 댓글 이력을 페이징 조회")
    @GetMapping("/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CommentHistoryResponseDto>> getCommentHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postAdminService.getCommentHistories(pageable));
    }

    @Operation(summary = "신고 히스토리 조회", description = "조건 없이 최신순으로 신고 이력을 페이징 조회")
    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PostReportHistoryResponseDto>> getPostReportHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postAdminService.getPostReportHistories(pageable));
    }
}
