package com.work.scheduler.util

import com.work.scheduler.common.config.JacksonConfiguration
import com.work.scheduler.common.interceptors.ExceptionHandlerAdvice
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class TestUtils {

  static createMvc(... controllers) {
    def mapper = new JacksonConfiguration().objectMapper()
    MockMvcBuilders.standaloneSetup(controllers)
            .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
            .setControllerAdvice(new ExceptionHandlerAdvice())
            .build()
  }
}
