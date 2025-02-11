package ru.yandex.practicum.model.hubs;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAction;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioCondition;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioAddedEvent extends HubEvent {
    private String name;
    private List<ScenarioCondition> conditions;
    private List<DeviceAction> actions;

    @Override
    public String getType() {
        return "SCENARIO_ADDED";
    }
}