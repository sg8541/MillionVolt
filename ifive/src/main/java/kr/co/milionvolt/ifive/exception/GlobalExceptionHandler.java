package kr.co.milionvolt.ifive.exception;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.milionvolt.ifive.domain.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiError> handleLoginException(LoginException ex, HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<ApiError> handleSignupException(SignupException ex, HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(DuplicateEmailException.class)
//    public ResponseEntity<ApiError> handleDuplicateEmailException(DuplicateEmailException ex, HttpServletRequest request) {
//        ApiError apiError = ApiError.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.CONFLICT.value())
//                .error(HttpStatus.CONFLICT.getReasonPhrase())
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(DuplicateNicknameException.class)
//    public ResponseEntity<ApiError> handleDuplicateNicknameException(DuplicateNicknameException ex, HttpServletRequest request) {
//        ApiError apiError = ApiError.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.CONFLICT.value())
//                .error(HttpStatus.CONFLICT.getReasonPhrase())
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
//    }

    // JWT 관련 예외 처리
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex, HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("유효하지 않은 토큰입니다.")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(StoreNotFoundException.class)
//    public ResponseEntity<ApiError> handleStoreNotFoundException(StoreNotFoundException ex, HttpServletRequest request) {
//        ApiError apiError = ApiError.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.NOT_FOUND.value())
//                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
//    }

//    @ExceptionHandler(MemberNotFoundException.class)
//    public ResponseEntity<ApiError> handleMemberNotFoundException(MemberNotFoundException ex, HttpServletRequest request) {
//        ApiError apiError = ApiError.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.NOT_FOUND.value())
//                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
//    }

//    @ExceptionHandler(InvalidPasswordResetException.class)
//    public ResponseEntity<ApiError> handleInvalidPasswordResetException(InvalidPasswordResetException ex, HttpServletRequest request) {
//        ApiError apiError = ApiError.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.BAD_REQUEST.value())
//                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
//                .message(String.join(", ", ex.getErrors()))
//                .path(request.getRequestURI())
//                .build();
//        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(FileStorageException.class)
//    public ResponseEntity<ApiError> handleFileStorageException(FileStorageException ex, HttpServletRequest request) {
//        ApiError apiError = ApiError.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
//                .message(ex.getMessage())
//                .path(request.getRequestURI())
//                .build();
//        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    // 기타 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(Exception ex, HttpServletRequest request) {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("내부 서버 오류가 발생했습니다.")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}