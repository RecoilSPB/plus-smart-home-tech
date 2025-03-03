package ru.yandex.practicum.model.hubs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceAction {

    @NotBlank
    String sensorId;

    @NotNull
    ActionType type;

    Integer value;
}