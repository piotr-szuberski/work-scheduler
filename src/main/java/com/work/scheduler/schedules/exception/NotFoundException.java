package com.work.scheduler.schedules.exception;


import com.work.scheduler.common.error.ErrorCode;

public class NotFoundException extends HttpException {

  private NotFoundException(ErrorCode code, String description, Object... args) {
    super(code, description, args);
  }

  public static NotFoundException notFoundException(
      ErrorCode code, String description, Object... args) {
    return new NotFoundException(code, description, args);
  }
}
