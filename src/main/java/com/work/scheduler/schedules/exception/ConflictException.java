package com.work.scheduler.schedules.exception;


import com.work.scheduler.common.error.ErrorCode;

public class ConflictException extends HttpException {

  private ConflictException(ErrorCode code, String description, Object... args) {
    super(code, description, args);
  }

  public static ConflictException conflictException(
      ErrorCode code, String description, Object... args) {
    return new ConflictException(code, description, args);
  }
}
