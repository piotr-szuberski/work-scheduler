package com.work.scheduler.schedules;

import static com.work.scheduler.schedules.ScheduleValidationException.scheduleValidationException;

import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ScheduleValidator {

  private final Clock clock;

  void validateSchedule(CreateScheduleDto schedule) {
    var today = LocalDateTime.now(clock);
    var shiftTime = ShiftTime.valueOf(schedule.shiftTime());
    if (schedule.shiftDate().isEqual(today.toLocalDate()) && today.toLocalTime().isAfter(shiftTime.getStartTime())) {
      throw scheduleValidationException("The requested shift has already begun.");
    }
  }
}
