package com.work.scheduler.schedules;

import static com.work.scheduler.schedules.exception.ConflictException.conflictException;
import static com.work.scheduler.schedules.exception.NotFoundException.notFoundException;
import static com.work.scheduler.schedules.exception.ScheduleValidationException.scheduleValidationException;

import com.work.scheduler.common.error.ErrorCode;
import com.work.scheduler.schedules.api.ScheduleDto;
import com.work.scheduler.schedules.repository.ScheduleRepository;
import com.work.scheduler.workers.repository.WorkerRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ScheduleValidator {

  private final Clock clock;
  private final ScheduleRepository scheduleRepository;
  private final WorkerRepository workerRepository;

  void validateCanCreate(ScheduleDto scheduleDto) {
    validateScheduleShiftTime(scheduleDto);
    validateWorkerExists(scheduleDto.email());
    validateScheduleNotExists(scheduleDto);
    validateOneSchedulePerDay(scheduleDto);
  }

  void validateScheduleExists(String id) {
    if (!scheduleRepository.existsById(id)) {
      throw notFoundException(
          ErrorCode.SCHEDULE_DOES_NOT_EXIST, "Schedule with id '%s' does not exist", id);
    }
  }

  private void validateScheduleNotExists(ScheduleDto scheduleDto) {
    if (scheduleRepository.existsById(scheduleDto.id())) {
      throw conflictException(
          ErrorCode.SCHEDULE_ALREADY_EXISTS,
          "Schedule with id '%s' already exists",
          scheduleDto.id());
    }
  }

  private void validateWorkerExists(String email) {
    if (!workerRepository.existsById(email)) {
      throw notFoundException(
          ErrorCode.WORKER_DOES_NOT_EXIST, "Worker with email '%s' is not registered", email);
    }
  }

  private void validateScheduleShiftTime(ScheduleDto schedule) {
    var today = LocalDateTime.now(clock);
    if (schedule.shiftDate().isBefore(today.toLocalDate())
        || schedule.shiftDate().isEqual(today.toLocalDate())
            && today.toLocalTime().isAfter(schedule.shiftTime().getStartTime())) {
      throw scheduleValidationException(
          ErrorCode.SHIFT_ALREADY_BEGUN, "The requested shift already begun.");
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
