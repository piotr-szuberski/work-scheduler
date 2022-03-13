package com.work.scheduler.common.interceptors;


import com.work.scheduler.common.error.ErrorDto;
import com.work.scheduler.common.error.ErrorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleException(MethodArgumentNotValidException exception) {
    var errorContainer = ErrorMapper.errorContainerFromValidations(exception);
    log.error("Input validation exception", exception);
    return ResponseEntity.badRequest().body(ErrorMapper.errorDto(errorContainer));
  }
}
