package ru.yandex.practicum.model.hubs;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.DeviceType;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceAddedEvent extends HubEvent {
    private String id;
    private DeviceType type;

    @Override
    public String getType() {
        return "DEVICE_ADDED";
    }
}