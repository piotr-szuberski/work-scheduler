package com.work.scheduler.common.error;

import static com.work.scheduler.common.error.ErrorCode.INPUT_VALIDATION;
import static com.work.scheduler.common.error.ErrorCode.INVALID_JSON;
import static com.work.scheduler.common.error.ErrorCode.UNKNOWN_REASON;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

public record ErrorDto(Set<ScheduleError> errors) {

  public static final String INVALID_INPUT_MSG = "Input is in invalid format";

  public static ErrorDto validationFailedError(BindException exception) {
    var errors =
        exception.getBindingResult().getFieldErrors().stream()
            .map(ErrorDto::createErrorDescription)
            .map(ErrorDto::inputError)
            .collect(Collectors.toSet());
    return new ErrorDto(errors);
  }

  public static ErrorDto inputInvalidError() {
    var error = new ScheduleError(INVALID_JSON, INVALID_INPUT_MSG);
    return new ErrorDto(Collections.singleton(error));
  }

  public static ErrorDto serviceError() {
    var error = new ScheduleError(UNKNOWN_REASON, "Unknown error");
    return new ErrorDto(Collections.singleton(error));
  }

  private static String createErrorDescription(FieldError error) {
    return String.format("%s: %s", error.getField(), error.getDefaultMessage());
  }

  private static ScheduleError inputError(String description) {
    return new ScheduleError(INPUT_VALIDATION, description);
  }
}
