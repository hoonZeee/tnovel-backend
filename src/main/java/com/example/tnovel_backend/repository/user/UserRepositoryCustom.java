package com.example.tnovel_backend.repository.user;

import com.example.tnovel_backend.controller.admin.dto.request.UserTotalSearchRequest;
import com.example.tnovel_backend.repository.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> searchUsers(UserTotalSearchRequest request, Pageable pageable);
}
