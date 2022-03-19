package com.work.scheduler.util

import com.work.scheduler.schedules.api.ScheduleDto
import com.work.scheduler.schedules.api.ShiftTime

class ScheduleDtoBuilder {

  def id = 'abc'
  def email = 'example@email.com'
  def shiftTime = ShiftTime.NIGHT
  def shiftDate = TestDateUtils.TODAY

  ScheduleDto build() {
    return new ScheduleDto(id, email, shiftTime, shiftDate)
  }
}
