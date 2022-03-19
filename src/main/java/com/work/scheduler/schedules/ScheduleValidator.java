package com.work.scheduler.schedules;

import static com.work.scheduler.schedules.exception.ScheduleValidationException.scheduleValidationException;

import com.work.scheduler.common.error.ErrorCode;
import com.work.scheduler.schedules.api.ScheduleDto;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ScheduleValidator {

  private final Clock clock;

  void validateSchedule(ScheduleDto schedule) {
    var today = LocalDateTime.now(clock);
    if (schedule.shiftDate().isBefore(today.toLocalDate())
        || schedule.shiftDate().isEqual(today.toLocalDate())
            && today.toLocalTime().isAfter(schedule.shiftTime().getStartTime())) {
      throw scheduleValidationException(
          ErrorCode.SHIFT_ALREADY_BEGUN, "The requested shift already begun.");
    }
  }
}
