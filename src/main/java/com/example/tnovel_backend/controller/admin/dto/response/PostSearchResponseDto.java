package com.example.tnovel_backend.controller.admin.dto.response;

import com.example.tnovel_backend.repository.post.entity.Post;
import com.example.tnovel_backend.repository.post.entity.vo.VisibleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostSearchResponseDto {

    private Integer postId;
    private String username;
    private String title;
    private VisibleStatus visibleStatus;
    private LocalDateTime createdAt;

    public static PostSearchResponseDto from(Post post) {
        return new PostSearchResponseDto(
                post.getId(),
                post.getUser().getUsername(),
                post.getTitle(),
                post.getVisibleStatus(),
                post.getCreatedAt()
        );
    }
}
