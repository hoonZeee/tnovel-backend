package com.example.tnovel_backend.repository.post;

import com.example.tnovel_backend.controller.admin.dto.request.PostSearchRequestDto;
import com.example.tnovel_backend.repository.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> searchPost(PostSearchRequestDto request, Pageable pageable);

    Page<Post> findAllOrderedByCreatedAt(Pageable pageable);
}
