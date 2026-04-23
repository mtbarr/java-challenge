package io.github.mtbarr.challenge.api.v1.controller;

import io.github.mtbarr.challenge.api.v1.response.ApiErrorResponse;
import io.github.mtbarr.challenge.exception.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalError(CommonException exception, HttpServletRequest request) {
        return ResponseEntity.status(exception.getStatus().value())
            .body(new ApiErrorResponse(
                exception.getStatus().value(),
                exception.getMessage(),
                request.getRequestURI(),
                request.getMethod()
            ));
    }
}