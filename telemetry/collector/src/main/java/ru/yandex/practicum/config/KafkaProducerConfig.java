package ru.yandex.practicum.config;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.yandex.practicum.model.hubs.HubEvent;
import ru.yandex.practicum.model.sensors.SensorEvent;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private Map<String, Object> producerConfigs() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    @Bean
    public ProducerFactory<String, SensorEvent> sensorProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, SensorEvent> sensorKafkaTemplate() {
        return new KafkaTemplate<>(sensorProducerFactory());
    }

    @Bean
    public ProducerFactory<String, HubEvent> hubProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, HubEvent> hubKafkaTemplate() {
        return new KafkaTemplate<>(hubProducerFactory());
    }
}