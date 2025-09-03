package com.example.tnovel_backend.exception;

import com.example.tnovel_backend.exception.domain.BusinessException;
import com.example.tnovel_backend.exception.error.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ExceptionAdvice {


    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException e) {
        ErrorCode c = e.getCode();
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(c.getHttpStatus(), c.getMessage());
        pd.setTitle(c.getHttpStatus().getReasonPhrase());
        pd.setProperty("code", c.name());
        return pd;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
        String message = stringify(e);
        log.warn("Validation error: {}", message);
        return build(HttpStatus.BAD_REQUEST, message);
    }

    // Service 단 ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatus(ResponseStatusException e) {
        log.warn("ResponseStatusException: {}", e.getReason());
        return build(HttpStatus.valueOf(e.getStatusCode().value()), e.getReason());
    }

    // 파라미터 Validation 실패 (@PathVariable, @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException e) {
        String message = stringify(e);
        log.warn("Constraint violation: {}", message);
        return build(HttpStatus.BAD_REQUEST, message);
    }


    // DTO Validation 메시지 가공
    private String stringify(MethodArgumentNotValidException exception) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorMessageBuilder.append(fieldError.getField()).append(": ");
            errorMessageBuilder.append(fieldError.getDefaultMessage()).append(", ");
        }
        errorMessageBuilder.delete(errorMessageBuilder.length() - 2, errorMessageBuilder.length());
        return errorMessageBuilder.toString();
    }

    // 파라미터 Validation 메시지 가공
    private String stringify(ConstraintViolationException exception) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errorMessageBuilder.append(violation.getPropertyPath()).append(": ");
            errorMessageBuilder.append(violation.getMessage()).append(", ");
        }
        errorMessageBuilder.delete(errorMessageBuilder.length() - 2, errorMessageBuilder.length());
        return errorMessageBuilder.toString();
    }

    // 표준 응답 생성 (RFC 7807)
    private ProblemDetail build(HttpStatus status, String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(status.getReasonPhrase());
        return problemDetail;
    }
}
