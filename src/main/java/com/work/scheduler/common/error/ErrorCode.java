package com.work.scheduler.common.error;

public enum ErrorCode {
  INPUT_VALIDATION,
  INVALID_JSON,
  SHIFT_ALREADY_BEGUN,
  SCHEDULE_ALREADY_EXISTS,
  SCHEDULE_DOES_NOT_EXIST,
  WORKER_DOES_NOT_EXIST,
  DAILY_LIMIT_FOR_WORKER_EXCEEDED,
}
