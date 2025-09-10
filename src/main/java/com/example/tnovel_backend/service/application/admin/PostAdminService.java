package com.example.tnovel_backend.service.application.admin;

import com.example.tnovel_backend.controller.admin.dto.request.PostSearchRequestDto;
import com.example.tnovel_backend.controller.admin.dto.response.AdminPostReportResponseDto;
import com.example.tnovel_backend.controller.admin.dto.response.PostSearchResponseDto;
import com.example.tnovel_backend.controller.admin.dto.response.history.CommentHistoryResponseDto;
import com.example.tnovel_backend.controller.admin.dto.response.history.PostHistoryResponseDto;
import com.example.tnovel_backend.controller.admin.dto.response.history.PostReportHistoryResponseDto;
import com.example.tnovel_backend.exception.domain.PostException;
import com.example.tnovel_backend.exception.error.PostErrorCode;
import com.example.tnovel_backend.repository.post.*;
import com.example.tnovel_backend.repository.post.entity.Post;
import com.example.tnovel_backend.repository.post.entity.PostHistory;
import com.example.tnovel_backend.repository.post.entity.PostReport;
import com.example.tnovel_backend.repository.post.entity.PostReportHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostAdminService {

    private final PostRepository postRepository;
    private final PostReportRepository postReportRepository;
    private final PostHistoryRepository postHistoryRepository;
    private final CommentHistoryRepository commentHistoryRepository;
    private final PostReportHistoryRepository postReportHistoryRepository;


    @Transactional(readOnly = true)
    public Page<AdminPostReportResponseDto> getAllReports(Pageable pageable) {
        Page<PostReport> reports = postReportRepository.findAllByOrderByCreatedAtDesc(pageable);
        return reports.map(AdminPostReportResponseDto::from);
    }

    @Transactional
    public void deleteReport(Integer reportId) {
        PostReport report = postReportRepository.findById(reportId)
                .orElseThrow(() -> new PostException(PostErrorCode.REPORT_NOT_FOUND));
        postReportRepository.delete(report);
        postReportHistoryRepository.save(
                PostReportHistory.create(
                        report.getId(),
                        report.getUser().
                                getUsername(),
                        report.getPost().getId(),
                        "DELETE"
                )
        );
    }

    @Transactional
    public void blindPostFromReport(Integer reportId) {
        PostReport report = postReportRepository.findById(reportId)
                .orElseThrow(() -> new PostException(PostErrorCode.REPORT_NOT_FOUND));
        Post post = report.getPost();
        post.setInvisible();
        postHistoryRepository.save(
                PostHistory.create(post.getId(), post.getUser().getUsername(), "INVISIBLE")
        );
    }


    public Page<PostSearchResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAllOrderedByCreatedAt(pageable)
                .map(PostSearchResponseDto::from);
    }

    public Page<PostSearchResponseDto> searchPosts(PostSearchRequestDto request, Pageable pageable) {
        return postRepository.searchPost(request, pageable)
                .map(PostSearchResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostHistoryResponseDto> getPostHistories(Pageable pageable) {
        return postHistoryRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(PostHistoryResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<CommentHistoryResponseDto> getCommentHistories(Pageable pageable) {
        return commentHistoryRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(CommentHistoryResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostReportHistoryResponseDto> getPostReportHistories(Pageable pageable) {
        return postReportHistoryRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(PostReportHistoryResponseDto::from);
    }
}
