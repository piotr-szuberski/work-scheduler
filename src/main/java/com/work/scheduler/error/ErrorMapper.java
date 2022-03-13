package com.work.scheduler.error;

import static com.work.scheduler.error.ErrorCode.INPUT_VALIDATION_ERROR;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

@UtilityClass
public class ErrorMapper {

  public static ErrorContainer errorContainerFromValidations(
      MethodArgumentNotValidException exception) {
    var errors =
        exception.getBindingResult().getFieldErrors().stream()
            .map(
                it ->
                    new Error(
                        it.getField(),
                        StringUtils.defaultString(it.getDefaultMessage(), "unknown")))
            .toList();
    return new ErrorContainer(exception, errors);
  }

  public static ErrorDto errorDto(ErrorContainer errorContainer) {
    return new ErrorDto(INPUT_VALIDATION_ERROR, errorContainer.errors());
  }
}
