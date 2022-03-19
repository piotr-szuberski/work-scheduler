package com.work.scheduler.common.error;

public record ScheduleError(ErrorCode code, String description) {

  public static ScheduleError of(ErrorCode code, String descriptionTemplate, Object... args) {
    var description = String.format(descriptionTemplate, args);
    return new ScheduleError(code, description);
  }
}
