package com.work.scheduler.schedules;


import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@Slf4j
class ScheduleController {

  @PutMapping("schedules/{id}")
  @ResponseStatus(HttpStatus.OK)
  void createOrUpdate(@PathVariable String id, @Valid @RequestBody CreateScheduleDto schedule) {
    log.debug("Creating new schedule: {} with id {}", schedule, id);

  }

  @DeleteMapping("schedules/{id}")
  @ResponseStatus(HttpStatus.OK)
  void update(@PathVariable String id) {
    log.debug("Deleting schedule with id {}", id);
  }


}
