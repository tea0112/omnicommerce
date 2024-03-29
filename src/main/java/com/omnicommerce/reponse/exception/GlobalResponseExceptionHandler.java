package com.omnicommerce.reponse.exception;

import com.omnicommerce.golobal.exception.ErrorCodes;
import com.omnicommerce.golobal.exception.LoginException;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.stream.Collectors;

import static com.omnicommerce.reponse.exception.ApiError.ApiErrorResponseEntities;

@Log4j2
@ControllerAdvice
public class GlobalResponseExceptionHandler {
  @ExceptionHandler({ConstraintViolationException.class, UserNotFoundException.class, LoginException.class, ServletException.class})
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

    return ApiErrorResponseEntities(log, ErrorCodes.E00001.name(), ErrorCodes.E00001.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR, null);
  }

  private ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
    return ApiErrorResponseEntities(log, ErrorCodes.E00003.name(), ErrorCodes.E00003.getMessage(), ex, HttpStatus.BAD_REQUEST, null);
  }

  private ResponseEntity<ApiError> handleLoginException(LoginException ex) {
    return ApiErrorResponseEntities(log, ErrorCodes.E00004.name(), ErrorCodes.E00004.getMessage(), ex, HttpStatus.UNAUTHORIZED, null);
  }

  private ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException violationsEx) {
    return ApiErrorResponseEntities(log, ErrorCodes.E00001.name(), ErrorCodes.E00001.getMessage(), violationsEx, HttpStatus.BAD_REQUEST, violationsEx.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
  }
}
