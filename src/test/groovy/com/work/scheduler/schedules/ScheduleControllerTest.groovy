package com.work.scheduler.schedules

import static com.work.scheduler.common.error.ErrorDto.INVALID_INPUT_MSG
import static com.work.scheduler.schedules.exception.ScheduleValidationException.scheduleValidationException
import static com.work.scheduler.util.TestDateUtils.FUTURE
import static com.work.scheduler.util.TestDateUtils.PAST
import static com.work.scheduler.util.TestDateUtils.TODAY
import static com.work.scheduler.util.TestUtils.createMvc
import static groovy.json.JsonOutput.toJson

import com.work.scheduler.common.error.ErrorCode
import com.work.scheduler.schedules.api.ScheduleController
import com.work.scheduler.schedules.api.ScheduleDto
import com.work.scheduler.schedules.api.ScheduleDto.Fields
import com.work.scheduler.util.TestDateUtils
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
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

    when:
    def response = mvc.perform(MockMvcRequestBuilders.post(PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(body)))
        .andReturn()
        .getResponse()

    then:
    response.status == 400
    response.contentAsString.contains(field as String)

    0 * _

    where:
    id   | email       | shiftTime | shiftDate | field             | msg
    ''   | EMAIL       | SHIFT     | FUTURE    | Fields.id         | 'empty id'
    null | EMAIL       | SHIFT     | FUTURE    | Fields.id         | 'null id'
    ID   | 'not-email' | SHIFT     | FUTURE    | Fields.email      | 'invalid email'
    ID   | null        | SHIFT     | FUTURE    | Fields.email      | 'null email'
    ID   | EMAIL       | 'invalid' | FUTURE    | INVALID_INPUT_MSG | 'invalid shiftTime'
    ID   | EMAIL       | null      | FUTURE    | Fields.shiftTime  | 'null shiftTime'
    ID   | EMAIL       | SHIFT     | PAST      | Fields.shiftDate  | 'past date'
    ID   | EMAIL       | SHIFT     | 'invalid' | INVALID_INPUT_MSG | 'past date'
    ID   | EMAIL       | SHIFT     | null      | INVALID_INPUT_MSG | 'past date'
  }

  def "Should return ok response on post request"() {
    when:
    def response = mvc.perform(MockMvcRequestBuilders.post(PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(validBody()))
        .andReturn()
        .getResponse()

    then:
    response.status == 201
    !response.contentAsString

    1 * scheduleService.bookSchedule(_)
  }

  def "Should return bad request when schedule validation failed"() {
    given:
    scheduleService.bookSchedule(_ as ScheduleDto) >> {
      throw scheduleValidationException(ErrorCode.SHIFT_ALREADY_BEGUN, 'description')
    }

    when:
    def response = mvc.perform(MockMvcRequestBuilders.post(PATH)
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
    def response = mvc.perform(MockMvcRequestBuilders.delete("$PATH/$ID"))
        .andReturn()
        .getResponse()

    then:
    response.status == 204
    !response.contentAsString

    1 * scheduleService.deleteSchedule(_)
  }

  def "Should call get schedules with valid dates"() {
    when:
    def response = mvc.perform(MockMvcRequestBuilders.get("$PATH$queryParams"))
        .andReturn()
        .getResponse()

    then:
    response.status == 200
    JSONAssert.assertEquals response.contentAsString, """{"schedules": []}""", JSONCompareMode.STRICT
    1 * scheduleService.getSchedules(*_) >> { from, to ->
      verifyAll {
        from == expectedDateFrom
        to == expectedDateTo
      }
      []
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
