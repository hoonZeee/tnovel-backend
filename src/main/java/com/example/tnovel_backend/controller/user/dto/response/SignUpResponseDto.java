package com.example.tnovel_backend.controller.user.dto.response;

import com.example.tnovel_backend.repository.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponseDto {
    private Integer id;
    private String username;
    private String name;
    private String profileImage;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private String accessToken;

    public static SignUpResponseDto from(User user, String accessToken) {
        return new SignUpResponseDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getProfileImage(),
                user.getRole().name(),
                user.getStatus().name(),
                user.getCreatedAt(),
                accessToken
        );
    }
}
