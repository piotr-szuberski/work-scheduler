package com.work.scheduler.schedules;


import com.work.scheduler.schedules.api.ScheduleDto;
import com.work.scheduler.schedules.api.ShiftTime;
import com.work.scheduler.schedules.repository.Schedule;
import lombok.experimental.UtilityClass;

@UtilityClass
class ScheduleMapper {

  static Schedule toEntity(ScheduleDto dto) {
    return Schedule.builder()
        .scheduleId(dto.id())
        .shiftDate(dto.shiftDate())
        .shiftTime(dto.shiftTime().name())
        .workerEmail(dto.email())
        .build();
  }

  static ScheduleDto toDto(Schedule schedule) {
    return new ScheduleDto(
        schedule.getScheduleId(),
        schedule.getWorkerEmail(),
        ShiftTime.valueOf(schedule.getShiftTime()),
        schedule.getShiftDate());
  }
}
