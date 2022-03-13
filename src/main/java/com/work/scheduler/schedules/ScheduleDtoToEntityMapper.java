package com.work.scheduler.schedules;


import com.work.scheduler.repository.schedules.ScheduleEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
class ScheduleDtoToEntityMapper {

  static ScheduleEntity scheduleDtoToEntity(String id, CreateScheduleDto dto) {
    return ScheduleEntity.builder()
        .scheduleId(id)
        .shiftDate(dto.shiftDate())
        .shiftTime(dto.shiftTime())
        .workerEmail(dto.email())
        .build();
  }
}
