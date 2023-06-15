package com.omnicommerce.reponse.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String errorCode;
    private String message;
    private String debugMessage;
    private List<ApiSubError> errors;

    private ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String errorCode, String message) {
        this();
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, String errorCode, String message, Throwable ex) {
        this();
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.debugMessage = ex.getMessage();
    }

    public static ApiError ApiErrors(Logger log, String errorCode, String userFriendlyMessage, Throwable ex, HttpStatus httpStatus) {
        ApiError apiError = null;
        if (log.isDebugEnabled()) {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, errorCode, userFriendlyMessage, ex);
        } else {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, errorCode, userFriendlyMessage);
        }
        return apiError;
    }

    public static ResponseEntity<ApiError> ApiErrorResponseEntities(Logger log, String errorCode, String userFriendlyMessage, Throwable ex, HttpStatus httpStatus) {
        ApiError apiError = null;
        if (log.isDebugEnabled()) {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, errorCode, userFriendlyMessage, ex);
        } else {
            apiError = new ApiError(HttpStatus.BAD_REQUEST, errorCode, userFriendlyMessage);
        }
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
