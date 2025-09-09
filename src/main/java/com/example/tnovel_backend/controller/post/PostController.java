package com.example.tnovel_backend.controller.post;

import com.example.tnovel_backend.controller.post.dto.request.PostCreateRequestDto;
import com.example.tnovel_backend.controller.post.dto.response.PostSimpleResponseDto;
import com.example.tnovel_backend.exception.domain.PostException;
import com.example.tnovel_backend.exception.error.PostErrorCode;
import com.example.tnovel_backend.service.application.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물", description = "게시물 관련 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "게시글 등록", description = "SWAGGER용 JSON으로 title, content, imageUrls 넘겨서 등록 -> 여러 첨부파일을 넣으려면 다음을 복붙해주세요 >  { \"title\": \"뉴욕\", \"content\": \"항상 꿈에 그리던곳 상상 그 이상이다.\", \"mediaList\": [ { \"url\": \"https://cdn.example.com/images/A.jpg\", \"mediaType\": \"IMAGE\" }, { \"url\": \"https://cdn.example.com/images/B.jpg\", \"mediaType\": \"IMAGE\" }, { \"url\": \"https://cdn.example.com/videos/C.mp4\", \"mediaType\": \"VIDEO\" } ] }")
    public ResponseEntity<PostSimpleResponseDto> createPost(
            @Valid @RequestBody PostCreateRequestDto request, //실제 운영시 @ModelAttribute
            Authentication authentication
    ) {
        String username = authentication.getName();
        PostSimpleResponseDto response = postService.createPost(request, username);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "피드 전체 조회", description = "pageIndex, size는 필수입니다.")
    @GetMapping("/posts")
    public ResponseEntity<Page<PostSimpleResponseDto>> getPosts(
            @RequestParam(name = "pageIndex") Integer pageIndex,
            @RequestParam(name = "size") Integer size
    ) {

        if (pageIndex < 0 || size <= 0) {
            throw new PostException(PostErrorCode.INVALID_PAGINATION_PARAMETER);
        }

        Pageable pageable = PageRequest.of(pageIndex, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostSimpleResponseDto> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 삭제", description = "자신이 작성한 게시글 삭제. 소프트 딜리트")
    @PatchMapping("/posts/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostSimpleResponseDto> deletePost(
            @Parameter(example = "1")
            @PathVariable Integer postId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        PostSimpleResponseDto response = postService.deletePost(postId, username);
        return ResponseEntity.ok(response);
    }

}
