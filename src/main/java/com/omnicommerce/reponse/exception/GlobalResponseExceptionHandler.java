package com.omnicommerce.reponse.exception;

import com.omnicommerce.golobal.exception.ErrorCodes;
import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@ControllerAdvice
public class GlobalResponseExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class, UserNotFoundException.class, LoginException.class})
    public ResponseEntity<ApiError> handleException(Exception ex) {
        if (ex instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) ex);
        }

        if (ex instanceof UserNotFoundException) {
            return handleUserNotFoundException((UserNotFoundException) ex);
        }

        if (ex instanceof LoginException) {
            return handleLoginException((LoginException) ex);
        }

        return ApiErrorWithDebugs(ErrorCodes.E00001.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        return ApiErrorWithDebugs(ErrorCodes.E00003.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> handleLoginException(LoginException ex) {
        return ApiErrorWithDebugs(ErrorCodes.E00004.getMessage(), ex, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException violationsEx) {
        return ApiErrorWithDebugs(ErrorCodes.E00002.getMessage(), violationsEx, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiError> ApiErrorWithDebugs(String userFriendlyMessage, Throwable ex, HttpStatus httpStatus) {
        ApiError apiError = null;
        if (GlobalResponseExceptionHandler.log.isDebugEnabled()) {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, userFriendlyMessage, ex);
        } else {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, userFriendlyMessage);
        }
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
