package com.example.tnovel_backend.exception.domain;

import com.example.tnovel_backend.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode code;


    public BusinessException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
