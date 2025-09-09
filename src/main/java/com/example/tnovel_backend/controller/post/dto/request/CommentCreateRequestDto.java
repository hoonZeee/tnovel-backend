package com.example.tnovel_backend.controller.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequestDto {

    @Schema(example = "뉴욕 참 좋죠")
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String contentText;

    @Schema(description = "게시물 ID", example = "1")
    @NotNull(message = "게시물 ID는 필수입니다.")
    private Integer postId;
}
