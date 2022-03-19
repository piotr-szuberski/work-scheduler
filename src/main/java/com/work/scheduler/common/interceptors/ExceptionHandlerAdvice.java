package com.work.scheduler.common.interceptors;


import com.work.scheduler.common.error.ErrorDto;
import com.work.scheduler.schedules.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorDto> handleValidationException(BindException exception) {
    var error = ErrorDto.validationFailedError(exception);
    log.error("Input validation exception. Message: {}", exception.getMessage(), exception);
    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleInvalidJsonException(
      HttpMessageNotReadableException exception) {
    log.error("Invalid input format. Message: {}", exception.getMessage(), exception);
    var errorDto = ErrorDto.inputInvalidError();
    return ResponseEntity.badRequest().body(errorDto);
  }

  @ExceptionHandler(HttpException.class)
  public ResponseEntity<ErrorDto> handleConflictException(HttpException exception) {
    log.error("Conflict during insert statement. Errors: {}", exception.getErrors(), exception);
    var errorDto = new ErrorDto(exception.getErrors());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
  }
}
