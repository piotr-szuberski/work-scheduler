package com.work.scheduler.schedules.exception;


import com.work.scheduler.common.error.ErrorCode;
import com.work.scheduler.common.error.ScheduleError;
import java.util.Set;
import lombok.Getter;

@Getter
public abstract class HttpException extends RuntimeException {

  private final Set<ScheduleError> errors;

  HttpException(ErrorCode code, String description, Object... args) {
    errors = Set.of(ScheduleError.of(code, description, args));
  }
}
