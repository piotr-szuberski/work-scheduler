package com.work.scheduler.schedules.api;


import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record ScheduleDto(
    @NotBlank String id,
    @Email @NotBlank String email,
    @NotNull ShiftTime shiftTime,
    @NotNull @FutureOrPresent LocalDate shiftDate) {}
