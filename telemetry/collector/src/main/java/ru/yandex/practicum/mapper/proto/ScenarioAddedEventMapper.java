package ru.yandex.practicum.mapper.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.hubs.*;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;

import java.time.Instant;

@Component
public class ScenarioAddedEventMapper implements HubEventProtoMapper {
    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public HubEvent map(HubEventProto event) {
        ScenarioAddedEventProto hubEvent = event.getScenarioAdded();

        ScenarioAddedEvent scenarioAddedEvent = ScenarioAddedEvent.builder()
                .hubId(event.getHubId())
                .timestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .name(hubEvent.getName())
                .actions(hubEvent.getActionList().stream().map(this::map).toList())
                .conditions(hubEvent.getConditionList().stream().map(this::map).toList())
                .build();
        return scenarioAddedEvent;
    }

    private DeviceAction map(DeviceActionProto deviceActionProto) {
        return DeviceAction.builder()
                .sensorId(deviceActionProto.getSensorId())
                .type(ActionType.valueOf(deviceActionProto.getType().name()))
                .value(deviceActionProto.getValue())
                .build();
    }

    private ScenarioCondition map(ScenarioConditionProto scenarioConditionProto) {
        if (scenarioConditionProto == null) {
            throw new IllegalArgumentException("ScenarioConditionProto cannot be null");
        }

        ScenarioCondition.ScenarioConditionBuilder builder = ScenarioCondition.builder()
                .sensorId(scenarioConditionProto.getSensorId())
                .conditionType(ConditionType.valueOf(scenarioConditionProto.getType().name()))
                .conditionOperation(ConditionOperation.valueOf(scenarioConditionProto.getOperation().name()));

        // Обработка oneof поля value
        switch (scenarioConditionProto.getValueCase()) {
            case BOOL_VALUE:
                builder.value(scenarioConditionProto.getBoolValue());
                break;
            case INT_VALUE:
                builder.value(scenarioConditionProto.getIntValue());
                break;
            case VALUE_NOT_SET:
            default:
                throw new IllegalArgumentException("Value is not set or unknown in ScenarioConditionProto");
        }

        return builder.build();
    }
}
