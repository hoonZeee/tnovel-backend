package com.example.tnovel_backend.controller.post.dto.response;

import com.example.tnovel_backend.repository.post.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentSimpleResponseDto {

    private Integer commentId;
    private String contentText;
    private String username;
    private LocalDateTime createdAt;

    public static CommentSimpleResponseDto from(Comment comment) {
        return new CommentSimpleResponseDto(
                comment.getId(),
                comment.getContentText(),
                comment.getUser().getUsername(),
                comment.getCreatedAt()
        );
    }
}
