package com.omnicommerce.reponse.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collection;

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
  private Collection<String> errorMessageList;

  private ApiError() {
    this.timestamp = LocalDateTime.now();
  }

  public ApiError(HttpStatus status, String errorCode, String message) {
    this();
    this.errorCode = errorCode;
    this.status = status;
    this.message = message;
  }

  public ApiError(HttpStatus status, String errorCode, String message, String debugMessage, Collection<String> errorMessageList) {
    this();
    this.status = status;
    this.errorCode = errorCode;
    this.message = message;
    this.debugMessage = debugMessage;
    this.errorMessageList = errorMessageList;
  }

  public ApiError(HttpStatus status, String errorCode, String message, Collection<String> errorMessageList) {
    this();
    this.status = status;
    this.errorCode = errorCode;
    this.message = message;
    this.errorMessageList = errorMessageList;
  }

  public static ApiError ApiErrors(Logger log, String errorCode, String userFriendlyMessage, Throwable ex, HttpStatus httpStatus, Collection<String> errorMessageList) {
    ApiError apiError = null;
    if (log.isDebugEnabled()) {
      apiError = new ApiError(httpStatus, errorCode, userFriendlyMessage, ex.getMessage(), errorMessageList);
    } else {
      apiError = new ApiError(httpStatus, errorCode, userFriendlyMessage, errorMessageList);
    }
    return apiError;
  }

  public static ResponseEntity<ApiError> ApiErrorResponseEntities(Logger log, String errorCode, String userFriendlyMessage, Throwable ex, HttpStatus httpStatus, Collection<String> errorMessageList) {
    ApiError apiError = null;
    if (log.isDebugEnabled()) {
      apiError = new ApiError(HttpStatus.BAD_REQUEST, errorCode, userFriendlyMessage, ex.getMessage(), errorMessageList);
    } else {
      apiError = new ApiError(HttpStatus.BAD_REQUEST, errorCode, userFriendlyMessage, errorMessageList);
    }
    return new ResponseEntity<>(apiError, httpStatus);
  }
}
