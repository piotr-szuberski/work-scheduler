package com.work.scheduler.schedules;

import static com.work.scheduler.schedules.exception.ConflictException.conflictException;
import static com.work.scheduler.schedules.exception.NotFoundException.notFoundException;

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
    validateScheduleExists(scheduleDto);
    validateOneSchedulePerDay(scheduleDto);
    scheduleValidator.validateSchedule(scheduleDto);
    var scheduleEntity = ScheduleMapper.toEntity(scheduleDto);
    scheduleRepository.save(scheduleEntity);
  }

  @Override
  public void deleteSchedule(String id) {
    validateToDeleteScheduleExists(id);
    scheduleRepository.deleteById(id);
  }

  @Override
  public List<ScheduleDto> getSchedules(LocalDate dateFrom, LocalDate dateTo) {
    return scheduleRepository.findAllByShiftDateBetween(dateFrom, dateTo).stream()
        .map(ScheduleMapper::toDto)
        .toList();
  }

  private void validateToDeleteScheduleExists(String id) {
    if (!scheduleRepository.existsById(id)) {
      throw notFoundException(
          ErrorCode.SCHEDULE_DOES_NOT_EXIST, "Schedule with id '%s' does not exist", id);
    }
  }

  private void validateScheduleExists(ScheduleDto scheduleDto) {
    if (scheduleRepository.existsById(scheduleDto.id())) {
      throw conflictException(
          ErrorCode.SCHEDULE_ALREADY_EXISTS,
          "Schedule with id '%s' already exists",
          scheduleDto.id());
    }
  }

  private void validateOneSchedulePerDay(ScheduleDto scheduleDto) {
    if (scheduleRepository.existsByWorkerEmailAndShiftDate(
        scheduleDto.email(), scheduleDto.shiftDate())) {
      throw conflictException(
          ErrorCode.DAILY_LIMIT_FOR_WORKER_EXCEEDED,
          "Worker with '%s' already has shift for '%s' booked",
          scheduleDto.email(),
          scheduleDto.shiftDate());
    }
  }
}
