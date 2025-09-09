package com.example.tnovel_backend.controller.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotBlank
    @Size(max = 20, message = "제목은 최대 20자까지 가능합니다.")
    @Schema(example = "뉴욕")
    private String title;

    @NotBlank
    @Size(max = 100, message = "내용은 최대 100자까지 가능합니다.")
    @Schema(example = "항상 꿈에 그리던곳 상상 그 이상이다. ")
    private String content;

    @NotNull(message = "첨부파일은 최소 1개 이상이어야 합니다.")
    @Size(min = 1, max = 5, message = "첨부파일은 1개 이상 5개 이하만 가능합니다.")
    private List<PostMediaCreateDto> mediaList;
    //private List<MultipartFile> images;  // 실제 운영시
}
