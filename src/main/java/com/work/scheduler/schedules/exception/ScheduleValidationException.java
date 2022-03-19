package com.work.scheduler.schedules.exception;


import com.work.scheduler.common.error.ErrorCode;
import com.work.scheduler.common.error.ScheduleError;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleValidationException extends RuntimeException {

  private final Set<ScheduleError> errors;

  public static ScheduleValidationException scheduleValidationException(
      ErrorCode code, String description) {
    return new ScheduleValidationException(Set.of(new ScheduleError(code, description)));
  }
}
