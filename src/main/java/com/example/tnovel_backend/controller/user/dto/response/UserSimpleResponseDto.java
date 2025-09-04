package com.example.tnovel_backend.controller.user.dto.response;

import com.example.tnovel_backend.repository.user.entity.User;
import com.example.tnovel_backend.repository.user.entity.vo.Role;
import com.example.tnovel_backend.repository.user.entity.vo.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleResponseDto {

    private Integer id;
    private String username;
    private String name;
    private String phoneNumberEncode;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;

    public static UserSimpleResponseDto from(User user) {
        return new UserSimpleResponseDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPhoneNumberEncode(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }

}
