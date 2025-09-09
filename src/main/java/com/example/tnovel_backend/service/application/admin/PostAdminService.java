package com.example.tnovel_backend.service.application.admin;

import com.example.tnovel_backend.controller.admin.dto.request.PostSearchRequestDto;
import com.example.tnovel_backend.controller.admin.dto.response.AdminPostReportResponseDto;
import com.example.tnovel_backend.controller.admin.dto.response.PostSearchResponseDto;
import com.example.tnovel_backend.exception.domain.PostException;
import com.example.tnovel_backend.exception.error.PostErrorCode;
import com.example.tnovel_backend.repository.post.PostReportRepository;
import com.example.tnovel_backend.repository.post.PostRepository;
import com.example.tnovel_backend.repository.post.entity.Post;
import com.example.tnovel_backend.repository.post.entity.PostReport;
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
    }

    @Transactional
    public void blindPostFromReport(Integer reportId) {
        PostReport report = postReportRepository.findById(reportId)
                .orElseThrow(() -> new PostException(PostErrorCode.REPORT_NOT_FOUND));
        Post post = report.getPost();
        post.setInvisible();
    }


    public Page<PostSearchResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAllOrderedByCreatedAt(pageable)
                .map(PostSearchResponseDto::from);
    }

    public Page<PostSearchResponseDto> searchPosts(PostSearchRequestDto request, Pageable pageable) {
        return postRepository.searchPost(request, pageable)
                .map(PostSearchResponseDto::from);
    }
}
