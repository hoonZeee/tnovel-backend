package com.example.tnovel_backend.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum PostErrorCode implements ErrorCode {
    EMPTY_IMAGE_FILE(HttpStatus.BAD_REQUEST, "이미지 한장은 필수입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지는 최대 5장까지 업로드 가능합니다");


    private final HttpStatus status;

    @Getter
    private final String message;

    PostErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }

}
