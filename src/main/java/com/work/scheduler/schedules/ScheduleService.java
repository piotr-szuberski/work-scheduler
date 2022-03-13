package com.work.scheduler.schedules;


import com.work.scheduler.repository.schedules.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ScheduleService {

  private final ScheduleRepository scheduleRepository;

  void saveSchedule(String id, CreateScheduleDto scheduleDto) {
    var scheduleEntity = ScheduleDtoToEntityMapper.scheduleDtoToEntity(id, scheduleDto);
    scheduleRepository.save(scheduleEntity);
  }
}
