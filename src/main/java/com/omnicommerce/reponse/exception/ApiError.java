package com.omnicommerce.reponse.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.omnicommerce.golobal.exception.ErrorCodes;
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
  private HttpStatus httpStatus;
  private String errorCode;
  private String message;
  private String debugMessage;
  private Collection<String> errorMessageList;

  private ApiError() {
    this.timestamp = LocalDateTime.now();
  }

  public static class Builder {
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private String errorCode = ErrorCodes.E00001.name();
    private String message = null;
    private String debugMessage = null;
    private Collection<String> errorMessageList = null;

    public Builder httpStatus(HttpStatus httpStatus) {
      this.httpStatus = httpStatus;
      return this;
    }

    public Builder errorCode(String errCode) {
      this.errorCode = errCode;
      return this;
    }

    public Builder message(String msg) {
      this.message = msg;
      return this;
    }

    public Builder debugMessage(String msg) {
      this.debugMessage = msg;
      return this;
    }

    public Builder errorMessageList(Collection<String> errs) {
      this.errorMessageList = errs;
      return this;
    }

    public ApiError build() {
      return new ApiError(this.httpStatus, this.errorCode, this.message, this.debugMessage, this.errorMessageList);
    }
  }

  public ApiError(HttpStatus httpStatus, String errorCode, String message) {
    this();
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public ApiError(HttpStatus httpStatus, String errorCode, String message, String debugMessage, Collection<String> errorMessageList) {
    this();
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.message = message;
    this.debugMessage = debugMessage;
    this.errorMessageList = errorMessageList;
  }

  public ApiError(HttpStatus httpStatus, String errorCode, String message, Collection<String> errorMessageList) {
    this();
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.message = message;
    this.errorMessageList = errorMessageList;
  }

  public static ApiError ApiErrors(Logger log, String errCode, String userFriendlyMessage, Throwable ex, HttpStatus httpStatus, Collection<String> errMsgList) {
    ApiError apiError = null;
    if (log.isDebugEnabled()) {
      apiError = new Builder().httpStatus(httpStatus).errorCode(errCode).message(userFriendlyMessage).debugMessage(ex.getMessage()).errorMessageList(errMsgList).build();
    } else {
      apiError = new Builder().httpStatus(httpStatus).errorCode(errCode).message(userFriendlyMessage).errorMessageList(errMsgList).build();
    }
    return apiError;
  }

  public static ResponseEntity<ApiError> ApiErrorResponseEntities(Logger log, String errCode, String userFriendlyMessage, Throwable ex, HttpStatus httpStatus, Collection<String> errMsgList) {
    ApiError apiError = null;
    if (log.isDebugEnabled()) {
      apiError = new Builder().httpStatus(httpStatus).errorCode(errCode).message(userFriendlyMessage).debugMessage(ex.getMessage()).errorMessageList(errMsgList).build();
    } else {
      apiError = new Builder().httpStatus(httpStatus).errorCode(errCode).message(userFriendlyMessage).errorMessageList(errMsgList).build();
    }
    return new ResponseEntity<>(apiError, httpStatus);
  }
}
