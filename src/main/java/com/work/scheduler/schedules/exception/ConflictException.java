package com.work.scheduler.schedules.exception;


import com.work.scheduler.common.error.ErrorCode;
import com.work.scheduler.common.error.ScheduleError;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConflictException extends RuntimeException {

  private final Set<ScheduleError> errors;

  public static ConflictException conflictException(
      ErrorCode code, String description, Object... args) {
    var error = new ScheduleError(code, String.format(description, args));
    return new ConflictException(Set.of(error));
  }
}
