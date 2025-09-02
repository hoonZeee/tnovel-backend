package com.example.tnovel_backend.controller.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocalSignUpRequestDto {
    private String phoneNumber;
    private String password;
    private String username;
    private String name;
}
