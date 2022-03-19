package com.work.scheduler.schedules

import static com.work.scheduler.util.ScheduleDtoFactory.scheduleDtoTodayMorning

import com.work.scheduler.common.error.ErrorCode
import com.work.scheduler.schedules.exception.ConflictException
import com.work.scheduler.schedules.exception.NotFoundException
import com.work.scheduler.schedules.repository.ScheduleRepository
import spock.lang.Specification

class ScheduleServiceImplTest extends Specification {

  def scheduleRepository = Mock(ScheduleRepository)
  def schedulevalidator = Mock(ScheduleValidator)

  def scheduleService = new ScheduleServiceImpl(scheduleRepository, schedulevalidator)

  def "Should not allow duplicate schedule for a worker creation"() {
    when:
    scheduleService.bookSchedule(scheduleDtoTodayMorning())

    then:
    def exception = thrown(ConflictException)
    exception.errors.stream().findFirst().value.code() == ErrorCode.DAILY_LIMIT_FOR_WORKER_EXCEEDED

    1 * scheduleRepository.existsByWorkerEmailAndShiftDate(*_) >> true
  }

  def "Should not allow duplicate schedule id"() {
    when:
    scheduleService.bookSchedule(scheduleDtoTodayMorning())

    then:
    def exception = thrown(ConflictException)
    exception.errors.stream().findFirst().value.code() == ErrorCode.SCHEDULE_ALREADY_EXISTS

    1 * scheduleRepository.existsById(*_) >> true
  }

  def "Should return not found when to delete schedule not present"() {
    when:
    scheduleService.deleteSchedule('abc')

    then:
    def exception = thrown(NotFoundException)
    exception.errors.stream().findFirst().value.code() == ErrorCode.SCHEDULE_DOES_NOT_EXIST

    1 * scheduleRepository.existsById(*_) >> false
    0 * _
  }

  def "Should  delete schedule"() {
    when:
    scheduleService.deleteSchedule('abc')

    then:
    1 * scheduleRepository.existsById(*_) >> true
    1 * scheduleRepository.deleteById(_)
    0 * _
  }
}
