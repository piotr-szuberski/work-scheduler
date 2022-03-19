package com.work.scheduler

import com.work.scheduler.util.TestDateUtils
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

import java.time.Clock

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(TestConfig)
class BaseIT extends Specification {

  @TestConfiguration
  static class TestConfig {
    @Bean
    @Primary
    Clock testClockBean() {
      TestDateUtils.testClock()
    }
  }
}
