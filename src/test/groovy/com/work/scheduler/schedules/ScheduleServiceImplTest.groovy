package com.work.scheduler.schedules

import static com.work.scheduler.util.ScheduleDtoFactory.scheduleDtoFuture

import com.work.scheduler.schedules.repository.ScheduleRepository
import spock.lang.Specification

class ScheduleServiceImplTest extends Specification {

  def scheduleRepository = Mock(ScheduleRepository)
  def scheduleValidator = Mock(ScheduleValidator)

  def scheduleService = new ScheduleServiceImpl(scheduleRepository, scheduleValidator)

  def "Should create schedule"() {
    when:
    scheduleService.createSchedule(scheduleDtoFuture())

    then:
    1 * scheduleValidator.validateCanCreate(_)
    1 * scheduleRepository.save(_)
    0 * _
  }

  def "Should delete schedule"() {
    when:
    scheduleService.deleteSchedule('abc')

    then:
    1 * scheduleValidator.validateScheduleExists(_)
    1 * scheduleRepository.deleteById(_)
    0 * _
  }
}
