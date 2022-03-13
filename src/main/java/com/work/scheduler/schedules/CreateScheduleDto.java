package com.work.scheduler.schedules;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

record CreateScheduleDto(

    @Email
    @NotBlank
    String email,

    @NotBlank
    @Pattern(regexp = "(MORNING|AFTERNOON|NIGHT)")
    String shiftTime,

    @NotNull
    @FutureOrPresent
    LocalDate shiftDate

) {

}
