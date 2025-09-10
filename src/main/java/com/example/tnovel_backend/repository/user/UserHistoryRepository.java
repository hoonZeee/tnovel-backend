package com.example.tnovel_backend.repository.user;

import com.example.tnovel_backend.repository.user.entity.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Integer> {
    Page<UserHistory> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
