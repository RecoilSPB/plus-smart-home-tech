package ru.yandex.practicum.service;

import ru.yandex.practicum.model.hubs.HubEvent;
import ru.yandex.practicum.model.sensors.SensorEvent;

public interface EventService {
    void processSensorEvent(SensorEvent sensorEvent);

    void processHubEvent(HubEvent hubEvent);
}