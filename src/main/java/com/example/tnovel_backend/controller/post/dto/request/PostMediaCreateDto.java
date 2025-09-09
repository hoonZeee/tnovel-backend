package com.example.tnovel_backend.controller.post.dto.request;

import com.example.tnovel_backend.repository.post.entity.vo.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostMediaCreateDto {
    @NotBlank(message = "URL은 필수입니다.")
    @Schema(example = "https://cdn.example.com/images/NewYork.jpg")
    private String url;

    @NotNull(message = "미디어 타입은 필수입니다.")
    @Schema(example = "IMAGE")
    private MediaType mediaType;

    
}
