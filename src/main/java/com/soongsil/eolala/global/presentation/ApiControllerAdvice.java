package com.soongsil.eolala.global.presentation;

import com.soongsil.eolala.global.support.error.GlobalErrorType;
import com.soongsil.eolala.global.support.error.GlobalException;
import com.soongsil.eolala.global.support.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;


@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e, HttpServletRequest request) {
        String path = request.getRequestURI();

        if (isStaticResourceRequest(path)) {
            log.debug("Static resource request (404): {} - {}", path, e.getMessage());
            return ResponseEntity.notFound().build();
        }
        log.error("Exception : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(GlobalErrorType.INTERNAL_ERROR), GlobalErrorType.INTERNAL_ERROR.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(GlobalErrorType.FAILED_REQUEST_VALIDATION), GlobalErrorType.FAILED_REQUEST_VALIDATION.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(GlobalErrorType.INVALID_REQUEST_ARGUMENT), GlobalErrorType.INVALID_REQUEST_ARGUMENT.getStatus());
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(GlobalException e) {
        log.error("GlobalException : {}", e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.error(e.getErrorType()), e.getErrorType().getStatus());
    }

    private boolean isStaticResourceRequest(String path) {
        if (path == null) return false;

        if (path.matches(".*\\.(php|ico|html|htm|env|txt|xml|json|js|css|png|jpg|jpeg|gif|svg|woff|woff2|ttf|eot)$")) {
            return true;
        }

        return path.contains("wp-admin") ||
               path.contains("wp-content") ||
               path.contains("wordpress") ||
               path.contains("vendor/phpunit") ||
               path.contains("admin") ||
               path.contains("phpmyadmin") ||
               path.contains("mysql") ||
               path.contains("login.html") ||
               path.contains("index.php") ||
               path.contains("config.php") ||
               path.contains("install.php") ||
               path.equals("/.env") ||
               path.equals("/robots.txt") ||
               path.equals("/sitemap.xml");
    }

}
