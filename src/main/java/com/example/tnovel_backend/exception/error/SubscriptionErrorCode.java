package com.example.tnovel_backend.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum SubscriptionErrorCode implements ErrorCode {
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구독을 찾을 수 없습니다."),
    SUBSCRIPTION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "이미 취소된 구독입니다."),
    SUBSCRIPTION_PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "구독 결제에 실패했습니다."),
    SUBSCRIPTION_PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "구독 결제 정보가 없습니다.");

    private final HttpStatus status;

    @Getter
    private final String message;


    SubscriptionErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
}
