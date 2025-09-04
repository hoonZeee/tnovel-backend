package com.example.tnovel_backend.repository.user;

import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.repository.user.entity.vo.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {


    Optional<User> findByUsername(String username);

    Optional<User> findByName(String name);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);

    Page<User> findByNameContainingIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);

    Page<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<User> findByStatusOrderByCreatedAtDesc(Status status, Pageable pageable);
}
