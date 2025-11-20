package com.ecommerce.productservice.exception;

import com.ecommerce.commonlibrary.response.ResponseError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Bắt lỗi Validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Gom tất cả các thông báo lỗi lại thành 1 chuỗi string ngăn cách bằng dấu phẩy
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        // Trả về ResponseError chuẩn
        return new ResponseEntity<>(
                new ResponseError(HttpStatus.BAD_REQUEST.value(), message),
                HttpStatus.BAD_REQUEST
        );
    }

    // 2. Bắt lỗi Runtime (Ví dụ: Không tìm thấy ID)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(
                new ResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    // 3. Bắt lỗi hệ thống khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleException(Exception ex) {
        return new ResponseEntity<>(
                new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}