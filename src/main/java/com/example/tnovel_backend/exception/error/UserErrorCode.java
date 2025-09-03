package com.example.tnovel_backend.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {

    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    LOGIN_ID_REQUIRED(HttpStatus.BAD_REQUEST, "아이디 또는 전화번호를 입력해주세요."),
    ACCOUNT_INACTIVE(HttpStatus.FORBIDDEN, "비활성화된 계정입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다.");

    private final HttpStatus status;

    @Getter
    private final String message;

    UserErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }


}
