package com.work.scheduler.error;


import java.util.List;

public record ErrorDto(ErrorCode code, List<Error> errors) {}
