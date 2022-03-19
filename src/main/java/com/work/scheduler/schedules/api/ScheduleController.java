package com.work.scheduler.schedules.api;


import com.work.scheduler.common.error.ErrorDto;
import com.work.scheduler.schedules.ScheduleService;
import com.work.scheduler.schedules.exception.ScheduleValidationException;
import java.time.Clock;
import java.time.LocalDate;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/schedules")
@Slf4j
@RequiredArgsConstructor
class ScheduleController {

  private static final long DAYS_FORWARD = 7L;

  private final ScheduleService scheduleService;
  private final Clock clock;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  void createOrUpdate(@Valid @RequestBody ScheduleDto schedule) {
    log.debug("Creating new schedule: {}", schedule);
    scheduleService.createSchedule(schedule);
  }

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteSchedule(@PathVariable String id) {
    log.debug("Deleting schedule with id {}", id);
    scheduleService.deleteSchedule(id);
  }

  @GetMapping
  ResponseEntity<ScheduleResponse> getSchedules(SchedulesQueryParams queryParams) {
    var now = LocalDate.now(clock);

    var schedules =
        scheduleService.getSchedules(
            queryParams.dateFrom().orElse(now),
            queryParams.dateTo().orElse(now.plusDays(DAYS_FORWARD)));

    return ResponseEntity.ok(new ScheduleResponse(schedules));
  }

  @ExceptionHandler(ScheduleValidationException.class)
  ResponseEntity<ErrorDto> handle(ScheduleValidationException exception) {
    var error = new ErrorDto(exception.getErrors());
    return ResponseEntity.badRequest().body(error);
  }
}
