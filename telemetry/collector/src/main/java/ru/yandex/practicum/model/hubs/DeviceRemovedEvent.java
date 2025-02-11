package ru.yandex.practicum.model.hubs;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceRemovedEvent extends HubEvent {
    private String id;

    @Override
    public String getType() {
        return "DEVICE_REMOVED";
    }
}