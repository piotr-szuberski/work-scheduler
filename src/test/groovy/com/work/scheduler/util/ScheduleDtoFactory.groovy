package com.work.scheduler.util

import com.work.scheduler.schedules.api.ScheduleDto
import com.work.scheduler.schedules.api.ShiftTime

class ScheduleDtoFactory {

  static ScheduleDto scheduleDtoTodayNight() {
    new ScheduleDtoBuilder(shiftTime: ShiftTime.NIGHT).build()
  }

  static ScheduleDto scheduleDtoTodayMorning() {
    new ScheduleDtoBuilder(shiftTime: ShiftTime.MORNING).build()
  }

  static ScheduleDto scheduleDtoTodayEvening() {
    new ScheduleDtoBuilder(shiftTime: ShiftTime.EVENING).build()
  }

  static ScheduleDto scheduleDtoFuture() {
    new ScheduleDtoBuilder(shiftDate: TestDateUtils.FUTURE).build()
  }
}
