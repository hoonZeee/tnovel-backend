package com.example.tnovel_backend.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum PostErrorCode implements ErrorCode {
    EMPTY_IMAGE_FILE(HttpStatus.BAD_REQUEST, "이미지 한장은 필수입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지는 최대 5장까지 업로드 가능합니다"),
    INVALID_PAGINATION_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 페이지네이션 파라미터입니다. pageIndex ≥ 0, size > 0 이어야 합니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    UNAUTHORIZED_POST_ACCESS(HttpStatus.FORBIDDEN, "해당 게시글의 권한이 없습니다."),
    DUPLICATE_REPORT(HttpStatus.BAD_REQUEST, "이미 신고한 게시물입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 신고를 찾을 수 없습니다.");


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
