package com.work.scheduler.schedules;


import com.work.scheduler.schedules.api.ScheduleDto;
import com.work.scheduler.schedules.repository.ScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ScheduleServiceImpl implements ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final ScheduleValidator scheduleValidator;

  @Override
  public void createSchedule(ScheduleDto scheduleDto) {
    scheduleValidator.validateCanCreate(scheduleDto);
    var scheduleEntity = ScheduleMapper.toEntity(scheduleDto);
    scheduleRepository.save(scheduleEntity);
  }

  @Override
  public void deleteSchedule(String id) {
    scheduleValidator.validateScheduleExists(id);
    scheduleRepository.deleteById(id);
  }

  @Override
  public List<ScheduleDto> getSchedules(LocalDate dateFrom, LocalDate dateTo) {
    return scheduleRepository.findAllByShiftDateBetween(dateFrom, dateTo).stream()
        .map(ScheduleMapper::toDto)
        .toList();
  }
}
