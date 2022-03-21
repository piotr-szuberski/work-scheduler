package com.work.scheduler.workers.api

import static com.work.scheduler.util.TestUtils.createMvc
import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.work.scheduler.workers.WorkerService
import org.hamcrest.Matchers
import org.springframework.http.MediaType
import spock.lang.Specification

class WorkersControllerTest extends Specification {

  static PATH = '/api/v1/workers'
  static EMAIL = 'example@email.com'

  def workerService = Mock(WorkerService)
  def mvc = createMvc(new WorkersController(workerService))

  def "Should call add workers"() {
    given:
    def body = [email: EMAIL]

    when:
    def response = mvc.perform(post(PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(body)))
        .andReturn()
        .getResponse()

    then:
    response.status == 201
    !response.contentAsString
    1 * workerService.addWorker(_)
    0 * _
  }

  def "Should call get workers"() {
    when:
    mvc.perform(get(PATH))
        .andExpect(status().isOk())
        .andExpect(jsonPath('$.workers').isArray())
        .andExpect(jsonPath('$.workers[0]', Matchers.is(EMAIL)))

    then:
    1 * workerService.getWorkers() >> [EMAIL]
    0 * _
  }
}
