package com.example.tnovel_backend.controller.admin;

import com.example.tnovel_backend.controller.admin.dto.request.PostSearchRequestDto;
import com.example.tnovel_backend.controller.admin.dto.response.AdminPostReportResponseDto;
import com.example.tnovel_backend.controller.admin.dto.response.PostSearchResponseDto;
import com.example.tnovel_backend.service.application.admin.PostAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "어드민 게시물", description = "어드민 게시물 관련 API")
@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostAdminService postAdminService;

    @Operation(summary = "게시물 통합 검색", description = "username, 작성일, 상태 조합 검색")
    @PostMapping("/search")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<Page<PostSearchResponseDto>> searchPosts(
            @RequestBody PostSearchRequestDto request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostSearchResponseDto> result = postAdminService.searchPosts(request, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "전체 게시물 조회", description = "조건 없이 최신순으로 게시물 전체 조회")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PostSearchResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostSearchResponseDto> result = postAdminService.getAllPosts(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "신고된 게시물 목록 조회", description = "신고 사유, 신고자, 게시글 요약 정보 제공 (최신순)")
    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminPostReportResponseDto>> getReportedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postAdminService.getAllReports(pageable));
    }

    @Operation(summary = "신고 삭제", description = "신고를 DB에서 하드 딜리트합니다.")
    @DeleteMapping("/reports/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable Integer reportId) {
        postAdminService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "신고된 게시물 블라인드", description = "해당 신고가 연결된 게시물을 INVISIBLE 처리합니다.")
    @DeleteMapping("/reports/{reportId}/blind")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> blindReportedPost(@PathVariable Integer reportId) {
        postAdminService.blindPostFromReport(reportId);
        return ResponseEntity.noContent().build();
    }
}
