package com.example.tnovel_backend.controller.post;

import com.example.tnovel_backend.controller.post.dto.request.CommentCreateRequestDto;
import com.example.tnovel_backend.controller.post.dto.response.CommentSimpleResponseDto;
import com.example.tnovel_backend.service.application.post.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "댓글", description = "댓글 관련 API")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글작성", description = "게시물에 댓글을 작성합니다.")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentSimpleResponseDto> createComment(
            @Valid @RequestBody CommentCreateRequestDto request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        CommentSimpleResponseDto response = commentService.createdComment(request, username);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 삭제", description = "자신이 작성한 댓글을 삭제합니다. (하드 딜리트)")
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteComment(
            @Parameter(example = "1")
            @PathVariable Integer commentId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        commentService.deleteComment(commentId, username);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글의 댓글 목록 조회", description = "댓글 최신순으로 10개씩 조회")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentSimpleResponseDto>> getCommentByPost(
            @Parameter(example = "1")
            @PathVariable Integer postId,

            @Parameter(example = "0")
            @RequestParam(name = "pageIndex") Integer pageIndex,

            @Parameter(example = "10")
            @RequestParam(name = "size") Integer size
    ) {
        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CommentSimpleResponseDto> comments = commentService.getCommentsByPost(postId, pageable);
        return ResponseEntity.ok(comments);
    }

}
