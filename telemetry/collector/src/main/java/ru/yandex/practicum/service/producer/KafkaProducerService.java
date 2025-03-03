package ru.yandex.practicum.service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.config.KafkaProducerProperties;
import ru.yandex.practicum.mapper.avro.HubEventMapper;
import ru.yandex.practicum.mapper.avro.SensorEventMapper;
import ru.yandex.practicum.model.sensors.SensorEvent;
import ru.yandex.practicum.model.hubs.HubEvent;
import ru.yandex.practicum.service.EventService;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService implements EventService {
    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaProducerProperties kafkaProperties;

    @Override
    public void processSensorEvent(SensorEvent sensorEvent) {
        send(kafkaProperties.getSensorEventsTopic(),
                sensorEvent.getHubId(),
                sensorEvent.getTimestamp().toEpochMilli(),
                SensorEventMapper.toSensorEventAvro(sensorEvent));
    }

    @Override
    public void processHubEvent(HubEvent hubEvent) {
        send(kafkaProperties.getHubEventsTopic(),
                hubEvent.getHubId(),
                hubEvent.getTimestamp().toEpochMilli(),
                HubEventMapper.toHubEventAvro(hubEvent));
    }

    private void send(String topic, String key, Long timestamp, SpecificRecordBase specificRecordBase) {
        log.info("Sending event to topic: {}, key: {}, timestamp: {}", topic, key, timestamp);
        producer.send(new ProducerRecord<>(topic, null, timestamp, key, specificRecordBase),
                (metadata, exception) -> {
                    if (exception != null) {
                        log.error("Failed to send event to topic {}: {}", topic, exception.getMessage());
                    } else {
                        log.info("Event sent successfully to topic {} at partition {}, offset {}",
                                topic, metadata.partition(), metadata.offset());
                    }
                });
    }
}
