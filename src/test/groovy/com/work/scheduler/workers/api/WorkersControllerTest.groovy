package com.work.scheduler.workers.api

import static com.work.scheduler.util.TestUtils.createMvc
import static groovy.json.JsonOutput.toJson

import com.work.scheduler.workers.WorkerService
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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
    def response = mvc.perform(MockMvcRequestBuilders.post(PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(body)))
        .andReturn()
        .getResponse()

    then:
    response.status == 201
    1 * workerService.addWorker(_)
    0 * _
  }

  def "Should call get workers"() {
    when:
    def response = mvc.perform(MockMvcRequestBuilders.get(PATH))
        .andReturn()
        .getResponse()

    then:
    response.status == 200
    JSONAssert.assertEquals """{"workers": ["$EMAIL"]}""", response.contentAsString, JSONCompareMode.STRICT
    1 * workerService.getWorkers() >> [EMAIL]
    0 * _
  }
}
