package com.example.tnovel_backend.repository.post;

import com.example.tnovel_backend.repository.post.entity.PostReportHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReportHistoryRepository extends JpaRepository<PostReportHistory, Integer> {
    Page<PostReportHistory> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
