package com.work.scheduler.util

import com.work.scheduler.schedules.api.ShiftTime

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class TestDateUtils {

  static TODAY = LocalDate.of(2022, 3, 18)
  static FUTURE = TODAY.plusDays(4L)
  static PAST = TODAY.minusDays(4L)
  static TODAY_NIGHT = LocalDateTime.of(TODAY, LocalTime.of(3, 0))
  static TODAY_NOON = LocalDateTime.of(TODAY, LocalTime.of(12, 0))
  static TODAY_EVENING = LocalDateTime.of(TODAY, LocalTime.of(16, 1))

  static TODAY_NIGHT_SHIFT_START = LocalDateTime.of(TODAY, ShiftTime.NIGHT.startTime)
  static TODAY_MORNING_SHIFT_START = LocalDateTime.of(TODAY, ShiftTime.MORNING.startTime)
  static TODAY_EVENING_SHIFT_START = LocalDateTime.of(TODAY, ShiftTime.EVENING.startTime)

  static Clock testClock(now = TODAY_NIGHT) {
    Clock.fixed(now.toInstant(ZoneOffset.UTC))
  }
}
