package ru.yandex.practicum.mapper.proto;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.model.hubs.HubEvent;

public interface HubEventProtoMapper {
    HubEventProto.PayloadCase getMessageType();

    HubEvent map(HubEventProto event);
}