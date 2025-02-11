package ru.yandex.practicum.service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.hubs.HubEvent;
import ru.yandex.practicum.model.sensors.SensorEvent;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, SensorEvent> sensorKafkaTemplate;
    private final KafkaTemplate<String, HubEvent> hubKafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, SensorEvent> sensorKafkaTemplate,
                                KafkaTemplate<String, HubEvent> hubKafkaTemplate) {
        this.sensorKafkaTemplate = sensorKafkaTemplate;
        this.hubKafkaTemplate = hubKafkaTemplate;
    }

    public void sendSensorEvent(SensorEvent event) {
        sensorKafkaTemplate.send("telemetry.sensors.v1", event.getId(), event);
    }

    public void sendHubEvent(HubEvent event) {
        hubKafkaTemplate.send("telemetry.hubs.v1", event.getHubId(), event);
    }
}
