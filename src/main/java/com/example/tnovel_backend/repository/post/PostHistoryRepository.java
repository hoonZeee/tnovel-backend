package com.example.tnovel_backend.repository.post;

import com.example.tnovel_backend.repository.post.entity.PostHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHistoryRepository extends JpaRepository<PostHistory, Integer> {
    Page<PostHistory> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
