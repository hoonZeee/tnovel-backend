package com.example.tnovel_backend.controller.post.dto.response;

import com.example.tnovel_backend.repository.post.entity.Post;
import com.example.tnovel_backend.repository.post.entity.PostMedia;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostSimpleResponseDto {

    private Integer postId;
    private String title;
    private String content;
    private Integer likeCount;

    private List<String> imageUrls;
    private List<String> videoUrls;

    private LocalDateTime createdAt;
    private String username;

    public static PostSimpleResponseDto from(Post post) {
        List<String> imageUrls = post.getPostMedias().stream()
                .filter(m -> m.getMediaType().name().equals("IMAGE"))
                .map(PostMedia::getImageUrl)
                .toList();

        List<String> videoUrls = post.getPostMedias().stream()
                .filter(m -> m.getMediaType().name().equals("VIDEO"))
                .map(PostMedia::getImageUrl)
                .toList();

        return new PostSimpleResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getLikeCount(),
                imageUrls,
                videoUrls,
                post.getCreatedAt(),
                post.getUser().getUsername()
        );
    }
}
