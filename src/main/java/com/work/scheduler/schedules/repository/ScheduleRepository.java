package com.work.scheduler.schedules.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<Schedule, String> {

  List<Schedule> findAllByShiftDateBetween(LocalDate shiftDateFrom, LocalDate shiftDateTo);

  boolean existsByWorkerEmailAndShiftDate(String workerEmail, LocalDate shiftDate);
}
