package com.work.scheduler.schedules;

import static com.work.scheduler.schedules.exception.ConflictException.conflictException;

import com.work.scheduler.common.error.ErrorCode;
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
  public void bookSchedule(ScheduleDto scheduleDto) {
    validateScheduleExists(scheduleDto.id());
    scheduleValidator.validateSchedule(scheduleDto);
    var scheduleEntity = ScheduleMapper.toEntity(scheduleDto);
    scheduleRepository.save(scheduleEntity);
  }

  @Override
  public void deleteSchedule(String id) {
    scheduleRepository.deleteById(id);
  }

  @Override
  public List<ScheduleDto> getSchedules(LocalDate dateFrom, LocalDate dateTo) {
    return scheduleRepository.findAllByShiftDateBetween(dateFrom, dateTo).stream()
        .map(ScheduleMapper::toDto)
        .toList();
  }

  private void validateScheduleExists(String scheduleId) {
    if (scheduleRepository.existsById(scheduleId)) {
      throw conflictException(
          ErrorCode.SCHEDULE_ALREADY_EXISTS, "Schedule with id '%s' already exists", scheduleId);
    }
  }
}
