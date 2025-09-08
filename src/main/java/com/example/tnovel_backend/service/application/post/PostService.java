package com.example.tnovel_backend.service.application.post;

import com.example.tnovel_backend.repository.post.PostMediaRepository;
import com.example.tnovel_backend.repository.post.PostReportRepository;
import com.example.tnovel_backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final PostReportRepository postReportRepository;


}
