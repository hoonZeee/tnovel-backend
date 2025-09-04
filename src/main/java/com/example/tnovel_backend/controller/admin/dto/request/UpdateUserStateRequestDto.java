package com.example.tnovel_backend.controller.admin.dto.request;

import com.example.tnovel_backend.repository.user.entity.vo.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserStateRequestDto {
    @Schema(example = "BANNED", description = "변경할 유저 상태")
    private Status status;
}
