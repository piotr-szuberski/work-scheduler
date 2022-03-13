package com.work.scheduler.schedules;

class ScheduleValidationException extends RuntimeException {

  private ScheduleValidationException(String message) {
    super(message);
  }

  static ScheduleValidationException scheduleValidationException(String message) {
    return new ScheduleValidationException(message);
  }
}
