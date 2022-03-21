package com.work.scheduler.schedules

import static com.work.scheduler.common.error.ErrorCode.INPUT_VALIDATION
import static com.work.scheduler.common.error.ErrorCode.INVALID_JSON
import static com.work.scheduler.common.error.ErrorDto.INVALID_INPUT_MSG
import static com.work.scheduler.schedules.exception.ScheduleValidationException.scheduleValidationException
import static com.work.scheduler.util.TestDateUtils.FUTURE
import static com.work.scheduler.util.TestDateUtils.PAST
import static com.work.scheduler.util.TestDateUtils.TODAY
import static com.work.scheduler.util.TestUtils.createMvc
import static groovy.json.JsonOutput.toJson
import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.work.scheduler.common.error.ErrorCode
import com.work.scheduler.schedules.api.ScheduleController
import com.work.scheduler.schedules.api.ScheduleDto
import com.work.scheduler.schedules.api.ScheduleDto.Fields
import com.work.scheduler.util.ScheduleDtoFactory
import com.work.scheduler.util.TestDateUtils
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification
import spock.lang.Unroll

class ScheduleControllerTest extends Specification {

  static PATH = '/api/v1/schedules'

  static ID = 'id'
  static EMAIL = 'valid@email.com'
  static SHIFT = 'NIGHT'
  static FORWARD_NOW = TODAY.plusDays(ScheduleController.DAYS_FORWARD)

  def scheduleService = Mock(ScheduleService)

  def mvc = createMvc(new ScheduleController(scheduleService, TestDateUtils.testClock()))

  @Unroll
  def "Should receive error response when #msg"() {
    given:
    def body = [
      id       : id,
      email    : email,
      shiftTime: shiftTime,
      shiftDate: "$shiftDate"
    ]

    expect:
    mvc.perform(post(PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(body)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath('$.errors').isArray())
        .andExpect(jsonPath('$.errors[0].code', is(errorCode.name())))
        .andExpect(jsonPath('$.errors[0].description', containsString(field)))

    where:
    id   | email       | shiftTime | shiftDate | field             | errorCode        | msg
    ''   | EMAIL       | SHIFT     | FUTURE    | Fields.id         | INPUT_VALIDATION | 'empty id'
    null | EMAIL       | SHIFT     | FUTURE    | Fields.id         | INPUT_VALIDATION | 'null id'
    ID   | 'not-email' | SHIFT     | FUTURE    | Fields.email      | INPUT_VALIDATION | 'invalid email'
    ID   | null        | SHIFT     | FUTURE    | Fields.email      | INPUT_VALIDATION | 'null email'
    ID   | EMAIL       | 'invalid' | FUTURE    | INVALID_INPUT_MSG | INVALID_JSON     | 'invalid shiftTime'
    ID   | EMAIL       | null      | FUTURE    | Fields.shiftTime  | INPUT_VALIDATION | 'null shiftTime'
    ID   | EMAIL       | SHIFT     | PAST      | Fields.shiftDate  | INPUT_VALIDATION | 'past date'
    ID   | EMAIL       | SHIFT     | 'invalid' | INVALID_INPUT_MSG | INVALID_JSON     | 'past date'
    ID   | EMAIL       | SHIFT     | null      | INVALID_INPUT_MSG | INVALID_JSON     | 'past date'
  }

  def "Should return ok response on post request"() {
    when:
    def response = mvc.perform(post(PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(validBody()))
        .andReturn()
        .getResponse()

    then:
    response.status == 201
    !response.contentAsString

    1 * scheduleService.createSchedule(_)
  }

  def "Should return bad request when schedule validation failed"() {
    given:
    scheduleService.createSchedule(_ as ScheduleDto) >> {
      throw scheduleValidationException(ErrorCode.SHIFT_ALREADY_BEGUN, 'description')
    }

    when:
    def response = mvc.perform(post(PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(validBody()))
        .andReturn()
        .getResponse()

    then:
    response.status == 400
    response.contentAsString
  }

  def "Should return ok response on delete request"() {
    when:
    def response = mvc.perform(delete("$PATH/$ID"))
        .andReturn()
        .getResponse()

    then:
    response.status == 204
    !response.contentAsString

    1 * scheduleService.deleteSchedule(_)
  }

  def "Should call get schedules with valid dates"() {
    given:
    def scheduleDto = ScheduleDtoFactory.scheduleDtoFuture()

    when:
    mvc.perform(get("$PATH$queryParams"))
        .andExpect(status().isOk())
        .andExpect(jsonPath('$.schedules').isArray())
        .andExpect(jsonPath('$.schedules[0].id', is(scheduleDto.id())))
        .andExpect(jsonPath('$.schedules[0].email', is(scheduleDto.email())))
        .andExpect(jsonPath('$.schedules[0].shiftTime', is(scheduleDto.shiftTime().name())))
        .andExpect(jsonPath('$.schedules[0].shiftDate', is(scheduleDto.shiftDate().toString())))

    then:
    1 * scheduleService.getSchedules(*_) >> { from, to ->
      verifyAll {
        from == expectedDateFrom
        to == expectedDateTo
      }
      [scheduleDto]
    }

    where:
    queryParams                      | expectedDateFrom | expectedDateTo
    ''                               | TODAY            | FORWARD_NOW
    "?dateFrom=$PAST&dateTo=$FUTURE" | PAST             | FUTURE
    "?dateFrom=$PAST"                | PAST             | FORWARD_NOW
    "?dateTo=$FUTURE"                | TODAY            | FUTURE
  }

  static validBody() {
    def body = [
      id       : ID,
      email    : EMAIL,
      shiftTime: SHIFT,
      shiftDate: "$FUTURE"
    ]
    toJson(body)
  }
}
