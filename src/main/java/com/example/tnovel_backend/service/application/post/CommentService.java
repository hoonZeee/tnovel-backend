package com.example.tnovel_backend.service.application.post;

import com.example.tnovel_backend.controller.post.dto.request.CommentCreateRequestDto;
import com.example.tnovel_backend.controller.post.dto.response.CommentSimpleResponseDto;
import com.example.tnovel_backend.exception.domain.PostException;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.PostErrorCode;
import com.example.tnovel_backend.exception.error.UserErrorCode;
import com.example.tnovel_backend.repository.post.CommentRepository;
import com.example.tnovel_backend.repository.post.PostRepository;
import com.example.tnovel_backend.repository.post.entity.Comment;
import com.example.tnovel_backend.repository.post.entity.Post;
import com.example.tnovel_backend.repository.user.UserRepository;
import com.example.tnovel_backend.repository.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    @Transactional(readOnly = true)
    public Page<CommentSimpleResponseDto> getCommentsByPost(Integer postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        return commentRepository.findByPost(post, pageable)
                .map(CommentSimpleResponseDto::from);

    }

    @Transactional
    public CommentSimpleResponseDto createdComment(CommentCreateRequestDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.create(
                dto.getContentText(),
                user,
                post
        );

        commentRepository.save(comment);

        return CommentSimpleResponseDto.from(comment);
    }
}
