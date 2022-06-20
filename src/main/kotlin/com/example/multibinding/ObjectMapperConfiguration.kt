package com.example.multibinding

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


@Configuration
class ObjectMapperConfiguration {

    @Bean
    fun jackson2ObjectMapperBuilder(): Jackson2ObjectMapperBuilder {
        return object : Jackson2ObjectMapperBuilder() {
            override fun configure(objectMapper: ObjectMapper) {
                super.configure(objectMapper)
                objectMapper
                    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                    .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            }
        }
    }

}