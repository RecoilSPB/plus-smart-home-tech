package ru.yandex.practicum.config;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka.config")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaProducerProperties {
    String bootstrapServers;
    String clientId;
    String producerKeySerializer;
    String producerValueSerializer;
    String sensorEventsTopic;
    String hubEventsTopic;
}
