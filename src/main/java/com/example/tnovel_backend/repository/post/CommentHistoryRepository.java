package com.example.tnovel_backend.repository.post;

import com.example.tnovel_backend.repository.post.entity.CommentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentHistoryRepository extends JpaRepository<CommentHistory, Integer> {
    Page<CommentHistory> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
