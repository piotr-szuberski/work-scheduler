package com.work.scheduler.schedules

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

import com.work.scheduler.schedules.exception.ScheduleValidationException
import spock.lang.Specification

class ScheduleValidatorTest extends Specification {

  def "Should throw when the shift has already begun"() {
    given:
    def validator = new ScheduleValidator(testClock(now))

    when:
    validator.validateSchedule(scheduleDto)

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
    def validator = new ScheduleValidator(testClock(now))

    when:
    validator.validateSchedule(scheduleDto)

    then:
    notThrown(ScheduleValidationException)

    where:
    now                                   | scheduleDto
    TODAY_NIGHT_SHIFT_START               | scheduleDtoTodayNight()
    TODAY_MORNING_SHIFT_START             | scheduleDtoTodayMorning()
    TODAY_EVENING_SHIFT_START             | scheduleDtoTodayEvening()
    TODAY_NIGHT_SHIFT_START.minusDays(1L) | scheduleDtoTodayNight()
  }
}
