package com.example.tnovel_backend.repository.post;

import com.example.tnovel_backend.repository.post.entity.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Integer> {
}
