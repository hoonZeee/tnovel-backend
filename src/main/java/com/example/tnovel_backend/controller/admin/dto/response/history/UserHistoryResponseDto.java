package com.example.tnovel_backend.controller.admin.dto.response.history;

import com.example.tnovel_backend.repository.user.entity.UserHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserHistoryResponseDto {
    private Integer id;
    private Integer userId;
    private String username;
    private String action;
    private LocalDateTime createdAt;

    public static UserHistoryResponseDto from(UserHistory history) {
        return new UserHistoryResponseDto(
                history.getId(),
                history.getUserId(),
                history.getUsername(),
                history.getAction(),
                history.getCreatedAt()
        );
    }
}
