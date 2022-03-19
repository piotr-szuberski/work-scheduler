package com.work.scheduler.common.error;


import java.util.List;

public record ErrorContainer(Throwable exception, List<ScheduleError> errors) {}
