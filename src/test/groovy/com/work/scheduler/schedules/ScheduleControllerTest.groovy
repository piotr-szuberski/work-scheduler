package com.work.scheduler.schedules


import com.work.scheduler.common.config.JacksonConfiguration
import com.work.scheduler.common.interceptors.ExceptionHandlerAdvice
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class ScheduleControllerTest extends Specification {

  static path = '/api/v1/schedules'

  static EMAIL = 'valid@email.com'

  static SHIFT = 'NIGHT'
  static validId = 'abc'
  static PAST = LocalDateTime.of(2022, 2, 20, 4, 23, 0)
  static FUTURE = PAST.withYear(3100)

  MockMvc mvc

  static mapper = new JacksonConfiguration().objectMapper()

  def setup() {
    mvc = MockMvcBuilders.standaloneSetup(new ScheduleController())
            .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
            .setControllerAdvice(new ExceptionHandlerAdvice())
            .build()
  }

  @Unroll
  def "Should receive error response when #msg"() {
    when:
    def response = mvc.perform(MockMvcRequestBuilders.put("$path/$validId")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
                "email": "$email",
                "shiftTime": "$shiftTime",
                "shiftDate": "$shiftDate"
            }"""))
            .andReturn()
            .getResponse()

    then:
    response.status == 400

    where:
    email       | shiftTime | shiftDate  | msg
    'not-email' | SHIFT     | FUTURE     | 'invalid email'
    null        | SHIFT     | FUTURE     | 'null email'
    EMAIL       | 'invalid' | FUTURE     | 'invalid shiftTime'
    EMAIL       | null      | FUTURE     | 'null shiftTime'
    EMAIL       | SHIFT     | PAST       | 'past date'
    EMAIL       | SHIFT     | null       | 'null date'
    EMAIL       | SHIFT     | 'not-date' | 'invalid date'
  }

  def "Should return ok response"() {
    when:
    def response = mvc.perform(MockMvcRequestBuilders.put("$path/$validId")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
                "email": "$EMAIL",
                "shiftTime": "$SHIFT",
                "shiftDate": "$FUTURE"
            }"""))
            .andReturn()
            .getResponse()

    then:
    response.status == 200
  }
}
