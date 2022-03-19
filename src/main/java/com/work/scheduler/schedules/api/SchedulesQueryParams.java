package com.work.scheduler.schedules.api;


import java.time.LocalDate;
import java.util.Optional;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@FieldNameConstants
record SchedulesQueryParams(
    @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> dateFrom,
    @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> dateTo) {}
