package com.work.scheduler.schedules

import com.work.scheduler.repository.schedules.ScheduleEntity
import com.work.scheduler.repository.schedules.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

import java.time.LocalDate

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BaseIT extends Specification {

  @Autowired
  ScheduleRepository repository

  def "Should save the schedule only once"() {
    given:
    def entity = ScheduleEntity.builder()
            .scheduleId('abc')
            .shiftTime('NIGHT')
            .shiftDate(LocalDate.of(2022, 5, 3))
            .workerEmail('test@worker.com')
            .build()

    when:
    repository.save(entity)
    repository.save(entity)

    then:
    def result = repository.findAll()
    result.size() == 1
    result[0] == entity
  }
}
