package com.work.scheduler.schedules

import static com.work.scheduler.common.error.ErrorCode.DAILY_LIMIT_FOR_WORKER_EXCEEDED
import static com.work.scheduler.common.error.ErrorCode.SCHEDULE_ALREADY_EXISTS
import static com.work.scheduler.common.error.ErrorCode.WORKER_DOES_NOT_EXIST
import static com.work.scheduler.util.ScheduleDtoFactory.scheduleDtoFuture
import static com.work.scheduler.util.ScheduleDtoFactory.scheduleDtoTodayEvening
import static com.work.scheduler.util.ScheduleDtoFactory.scheduleDtoTodayMorning
import static com.work.scheduler.util.ScheduleDtoFactory.scheduleDtoTodayNight
import static com.work.scheduler.util.TestDateUtils.TODAY_EVENING
import static com.work.scheduler.util.TestDateUtils.TODAY_EVENING_SHIFT_START
import static com.work.scheduler.util.TestDateUtils.TODAY_MORNING_SHIFT_START
import static com.work.scheduler.util.TestDateUtils.TODAY_NIGHT
import static com.work.scheduler.util.TestDateUtils.TODAY_NIGHT_SHIFT_START
import static com.work.scheduler.util.TestDateUtils.TODAY_NOON
import static com.work.scheduler.util.TestDateUtils.testClock

import com.work.scheduler.common.error.ErrorCode
import com.work.scheduler.schedules.exception.ConflictException
import com.work.scheduler.schedules.exception.NotFoundException
import com.work.scheduler.schedules.exception.ScheduleValidationException
import com.work.scheduler.schedules.repository.ScheduleRepository
import com.work.scheduler.workers.repository.WorkerRepository
import spock.lang.Specification
import spock.lang.Unroll

class ScheduleValidatorTest extends Specification {

  def scheduleRepository = Mock(ScheduleRepository)
  def workerRepository = Mock(WorkerRepository)

  def "Should throw when the shift has already begun"() {
    given:
    def validator = new ScheduleValidator(testClock(now), scheduleRepository, workerRepository)

    when:
    validator.validateScheduleShiftTime(scheduleDto)

    then:
    thrown(ScheduleValidationException)

    where:
    now                      | scheduleDto
    TODAY_NIGHT              | scheduleDtoTodayNight()
    TODAY_NOON               | scheduleDtoTodayMorning()
    TODAY_EVENING            | scheduleDtoTodayEvening()
    TODAY_NIGHT.plusDays(1L) | scheduleDtoTodayEvening()
  }

  def "Should not throw when schedule in the future"() {
    given:
    def validator = new ScheduleValidator(testClock(now), scheduleRepository, workerRepository)

    when:
    validator.validateScheduleShiftTime(scheduleDto)

    then:
    notThrown(ScheduleValidationException)

    where:
    now                                   | scheduleDto
    TODAY_NIGHT_SHIFT_START               | scheduleDtoTodayNight()
    TODAY_MORNING_SHIFT_START             | scheduleDtoTodayMorning()
    TODAY_EVENING_SHIFT_START             | scheduleDtoTodayEvening()
    TODAY_NIGHT_SHIFT_START.minusDays(1L) | scheduleDtoTodayNight()
  }

  @Unroll
  def "Should throw creation validation when #msg"() {
    given:
    scheduleRepository.existsById(_) >> scheduleExists
    scheduleRepository.existsByWorkerEmailAndShiftDate(*_) >> taken
    workerRepository.existsById(_) >> workerExists

    def validator = new ScheduleValidator(testClock(TODAY_NIGHT), scheduleRepository, workerRepository)

    when:
    validator.validateCanCreate(scheduleDtoFuture())

    then:
    def exception = thrown(exceptionType)
    exception.errors.stream().findFirst().value.code() == code

    where:
    scheduleExists | workerExists | taken | exceptionType     | code                            | msg
    false          | true         | true  | ConflictException | DAILY_LIMIT_FOR_WORKER_EXCEEDED | 'already booked'
    false          | false        | false | NotFoundException | WORKER_DOES_NOT_EXIST           | 'worker not exists'
    true           | true         | true  | ConflictException | SCHEDULE_ALREADY_EXISTS         | 'schedule exists'
  }

  def "Should not throw exception when valid creation"() {
    given:
    scheduleRepository.existsById(_) >> false
    scheduleRepository.existsByWorkerEmailAndShiftDate(*_) >> false
    workerRepository.existsById(_) >> true

    def validator = new ScheduleValidator(testClock(TODAY_NIGHT), scheduleRepository, workerRepository)

    when:
    validator.validateCanCreate(scheduleDtoFuture())

    then:
    notThrown(Exception)
  }

  def "Should throw not found when delete inexistent schedule"() {
    given:
    scheduleRepository.existsById(_) >> false

    def validator = new ScheduleValidator(testClock(TODAY_NIGHT), scheduleRepository, workerRepository)

    when:
    validator.validateScheduleExists('id')

    then:
    def exception = thrown(NotFoundException)
    exception.errors.stream().findFirst().value.code() == ErrorCode.SCHEDULE_DOES_NOT_EXIST
  }
}
