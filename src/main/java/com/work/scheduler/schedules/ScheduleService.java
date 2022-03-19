package com.work.scheduler.schedules;


import com.work.scheduler.schedules.api.ScheduleDto;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

  void bookSchedule(ScheduleDto scheduleDto);

  void deleteSchedule(String id);

  List<ScheduleDto> getSchedules(LocalDate dateFrom, LocalDate dateTo);
}
