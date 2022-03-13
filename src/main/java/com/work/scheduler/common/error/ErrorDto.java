package com.work.scheduler.common.error;


import java.util.List;

public record ErrorDto(ErrorCode code, List<Error> errors) {}
