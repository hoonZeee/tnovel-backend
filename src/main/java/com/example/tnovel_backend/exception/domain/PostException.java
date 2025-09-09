package com.example.tnovel_backend.exception.domain;

import com.example.tnovel_backend.exception.error.PostErrorCode;

public class PostException extends BusinessException {
    public PostException(PostErrorCode code) {
        super(code);
    }
}
