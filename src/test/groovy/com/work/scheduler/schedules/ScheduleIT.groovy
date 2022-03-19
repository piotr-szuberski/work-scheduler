package com.work.scheduler.schedules

import static com.work.scheduler.util.ScheduleDtoFactory.scheduleDtoFuture
import static com.work.scheduler.util.TestDateUtils.FUTURE
import static com.work.scheduler.util.TestDateUtils.PAST
import static com.work.scheduler.util.TestDateUtils.TODAY

import com.work.scheduler.BaseIT
import com.work.scheduler.schedules.repository.ScheduleRepository
import com.work.scheduler.workers.repository.Worker
import com.work.scheduler.workers.repository.WorkerRepository
import org.springframework.beans.factory.annotation.Autowired

class ScheduleIT extends BaseIT {

  @Autowired
  ScheduleService scheduleService

  @Autowired
  ScheduleRepository scheduleRepository

  @Autowired
  WorkerRepository workerRepository

  def setup() {
    scheduleRepository.deleteAll()
  }

  def "Should save the schedule and return valid output"() {
    given:
    def scheduleDto = scheduleDtoFuture()
    workerRepository.save(new Worker(scheduleDto.email()))

    when:
    scheduleService.bookSchedule(scheduleDto)

    and:
    def result = scheduleService.getSchedules(dateFrom, dateTo)

    then:
    result == expectedSchedules

    where:
    dateFrom | dateTo | expectedSchedules
    PAST     | TODAY  | []
    FUTURE   | FUTURE | [scheduleDtoFuture()]
  }
}
