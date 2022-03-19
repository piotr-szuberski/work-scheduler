package com.work.scheduler.schedules.repository;


import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedule {

  @Id private String scheduleId;
  private LocalDate shiftDate;
  private String shiftTime;
  private String workerEmail;
}
