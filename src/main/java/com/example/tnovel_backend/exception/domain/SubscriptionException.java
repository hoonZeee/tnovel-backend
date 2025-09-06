package com.example.tnovel_backend.exception.domain;

import com.example.tnovel_backend.exception.error.SubscriptionErrorCode;

public class SubscriptionException extends BusinessException {

    public SubscriptionException(SubscriptionErrorCode code) {
        super(code);
    }
}
