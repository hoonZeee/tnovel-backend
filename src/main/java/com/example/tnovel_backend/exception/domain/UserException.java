package com.example.tnovel_backend.exception.domain;

import com.example.tnovel_backend.exception.error.UserErrorCode;
import lombok.Getter;

@Getter
public class UserException extends BusinessException{

    public UserException(UserErrorCode code) {
        super(code);
    }

}
