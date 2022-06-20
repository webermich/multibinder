package com.example.multibinding

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Repartitioned
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.support.serializer.JsonSerde
import java.util.function.Function


data class MyKafkaInEvent(val title: String, val x: Int)
data class MyKafkaOutEvent(val title: String, val x: Int)

@SpringBootApplication
class MultibindingApplication() {
    @Bean
    fun myKafkaInEventSerde(objectMapper: ObjectMapper): Serde<MyKafkaInEvent> {
        return JsonSerde(MyKafkaInEvent::class.java, objectMapper)
    }

    @Bean
    fun myKafkaOutEventSerde(objectMapper: ObjectMapper): Serde<MyKafkaOutEvent> {
        return JsonSerde(MyKafkaOutEvent::class.java, objectMapper)
    }

    @Bean
    fun kafkaEventProcessor(
        myKafkaInEventSerde: Serde<MyKafkaInEvent>,
        myKafkaOutEventSerde: Serde<MyKafkaOutEvent>
    )
            : Function<KStream<String, MyKafkaInEvent>, KStream<String, MyKafkaOutEvent>> {
        return Function<KStream<String, MyKafkaInEvent>, KStream<String, MyKafkaOutEvent>> { input ->
            input
				.map { key, value -> KeyValue(key, MyKafkaOutEvent(value.title, value.x)) }
				.repartition(Repartitioned.with(Serdes.String(), myKafkaOutEventSerde))
        }
    }

}

fun main(args: Array<String>) {
    runApplication<MultibindingApplication>(*args)
}
