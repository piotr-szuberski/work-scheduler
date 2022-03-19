package com.work.scheduler.schedules

import static com.work.scheduler.util.ScheduleDtoFactory.scheduleDtoFuture
import static com.work.scheduler.util.TestDateUtils.FUTURE
import static com.work.scheduler.util.TestDateUtils.PAST
import static com.work.scheduler.util.TestDateUtils.TODAY

import com.work.scheduler.BaseIT
import com.work.scheduler.common.error.ErrorCode
import com.work.scheduler.schedules.api.ShiftTime
import com.work.scheduler.schedules.exception.ConflictException
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
    scheduleService.createSchedule(scheduleDto)

    and:
    def result = scheduleService.getSchedules(dateFrom, dateTo)

    then:
    result == expectedSchedules

    where:
    dateFrom | dateTo | expectedSchedules
    PAST     | TODAY  | []
    FUTURE   | FUTURE | [scheduleDtoFuture()]
  }

  def "Should not allow multiple shifts for one day for one worker"() {
    given:
    def scheduleEvening = scheduleDtoFuture('id1', ShiftTime.EVENING)
    def scheduleNight = scheduleDtoFuture('id2', ShiftTime.NIGHT)
    workerRepository.save(new Worker(scheduleEvening.email()))

    when:
    scheduleService.createSchedule(scheduleNight)
    scheduleService.createSchedule(scheduleEvening)

    then:
    def exception = thrown(ConflictException)
    exception.errors.stream().findFirst().value.code() == ErrorCode.DAILY_LIMIT_FOR_WORKER_EXCEEDED
  }
}
