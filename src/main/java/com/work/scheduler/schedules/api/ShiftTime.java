package com.work.scheduler.schedules.api;


import java.time.LocalTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShiftTime {
  MORNING(8),
  EVENING(16),
  NIGHT(0);

  ShiftTime(Integer startTime) {
    this.startTime = LocalTime.of(startTime, 0);
  }

  @Getter private final LocalTime startTime;
}
