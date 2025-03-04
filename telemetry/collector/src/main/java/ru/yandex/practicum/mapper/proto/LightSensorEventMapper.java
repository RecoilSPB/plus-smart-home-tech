package ru.yandex.practicum.mapper.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.sensors.LightSensorEvent;
import ru.yandex.practicum.model.sensors.SensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.time.Instant;

@Component
public class LightSensorEventMapper implements SensorEventProtoMapper {
    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SensorEvent map(SensorEventProto event) {
        LightSensorProto sensorEvent = event.getLightSensorEvent();

        LightSensorEvent lightSensorEvent = LightSensorEvent.builder()
                .id(event.getId())
                .hubId(event.getHubId())
                .timestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .linkQuality(sensorEvent.getLinkQuality())
                .luminosity(sensorEvent.getLuminosity())
                .build();
        return lightSensorEvent;
    }
}
