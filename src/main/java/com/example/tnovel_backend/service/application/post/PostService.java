package com.example.tnovel_backend.service.application.post;

import com.example.tnovel_backend.controller.post.dto.request.PostCreateRequestDto;
import com.example.tnovel_backend.controller.post.dto.request.PostMediaCreateDto;
import com.example.tnovel_backend.controller.post.dto.response.PostReportResponseDto;
import com.example.tnovel_backend.controller.post.dto.response.PostSimpleResponseDto;
import com.example.tnovel_backend.exception.domain.PostException;
import com.example.tnovel_backend.exception.domain.UserException;
import com.example.tnovel_backend.exception.error.PostErrorCode;
import com.example.tnovel_backend.exception.error.UserErrorCode;
import com.example.tnovel_backend.repository.post.*;
import com.example.tnovel_backend.repository.post.entity.*;
import com.example.tnovel_backend.repository.post.entity.vo.ReportReason;
import com.example.tnovel_backend.repository.post.entity.vo.VisibleStatus;
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
    private final PostHistoryRepository postHistoryRepository;
    private final PostReportHistoryRepository postReportHistoryRepository;

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
        postHistoryRepository.save(PostHistory.create(post.getId(), user.getUsername(), "CREATE"));

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

        postHistoryRepository.save(PostHistory.create(post.getId(), username, "DELETE"));

        return PostSimpleResponseDto.from(post);
    }

    @Transactional
    public PostReportResponseDto reportPost(Integer postId, ReportReason reason, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        //중복 신고 방어 로직
        if (postReportRepository.existsByUserIdAndPostId(user.getId(), postId)) {
            throw new PostException(PostErrorCode.DUPLICATE_REPORT);
        }

        PostReport report = PostReport.create(reason, user, post);
        postReportRepository.save(report);

        //누적 신고가 10회 이상이면 INVISIBLE 로 업데이트 하는 로직
        long reportCount = postReportRepository.countByPostId(postId);
        if (reportCount >= 10 && post.getVisibleStatus() == VisibleStatus.VISIBLE) {
            post.setInvisible();
        }

        postReportHistoryRepository.save(
                PostReportHistory.create(report.getId(), user.getUsername(), post.getId(), "REPORT")
        );

        return PostReportResponseDto.from(report);
    }


}
