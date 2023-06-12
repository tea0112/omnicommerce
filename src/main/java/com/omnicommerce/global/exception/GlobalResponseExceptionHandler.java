package com.omnicommerce.global.exception;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@ControllerAdvice
public class GlobalResponseExceptionHandler {
    @ExceptionHandler({UserException.class})
    public ResponseEntity<Object> handleException(Exception e) {
        if (e instanceof UserException) {
            return handleUserException((UserException) e);
        }

        return new ResponseEntity<>("Internal exception", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> handleUserException(UserException userException) {
        return new ResponseEntity<Object>(ApiErrorWithDebugs(log, "having user exception", userException), HttpStatus.BAD_REQUEST);
    }

    private ApiError ApiErrorWithDebugs(Logger logger, String userFriendlyMessage, Throwable ex) {
        ApiError apiError = null;
        if (logger.isDebugEnabled()) {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, userFriendlyMessage, ex);
        } else {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, userFriendlyMessage);
        }
        return apiError;
    }
}
