package com.example.tnovel_backend.service.application.post;

import com.example.tnovel_backend.controller.post.dto.request.PostCreateRequestDto;
import com.example.tnovel_backend.controller.post.dto.request.PostMediaCreateDto;
import com.example.tnovel_backend.controller.post.dto.response.PostSimpleResponseDto;
import com.example.tnovel_backend.exception.domain.PostException;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.PostErrorCode;
import com.example.tnovel_backend.exception.error.UserErrorCode;
import com.example.tnovel_backend.repository.post.PostMediaRepository;
import com.example.tnovel_backend.repository.post.PostReportRepository;
import com.example.tnovel_backend.repository.post.PostRepository;
import com.example.tnovel_backend.repository.post.entity.Post;
import com.example.tnovel_backend.repository.post.entity.PostMedia;
import com.example.tnovel_backend.repository.user.UserRepository;
import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.service.domain.post.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final PostReportRepository postReportRepository;
    private final UserRepository userRepository;
    //private final ImageUtil imageUtil;

    @Transactional(readOnly = true)
    public Page<PostSimpleResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostSimpleResponseDto::from);
    }

    @Transactional
    public PostSimpleResponseDto createPost(PostCreateRequestDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Post post = Post.create(dto.getTitle(), dto.getContent(), user);
        postRepository.save(post);

        for (PostMediaCreateDto mediaDto : dto.getMediaList()) {
            PostMedia media = PostMedia.create(
                    mediaDto.getUrl(),
                    mediaDto.getMediaType(),
                    post
            );
            postMediaRepository.save(media);
            post.addMedia(media);
        }

        return PostSimpleResponseDto.from(post);
    }

    @Transactional
    public PostSimpleResponseDto deletePost(Integer postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getUsername().equals(username)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_POST_ACCESS);
        }

        post.delete();
        return PostSimpleResponseDto.from(post);
    }


}
