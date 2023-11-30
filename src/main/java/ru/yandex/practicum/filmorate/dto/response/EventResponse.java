package ru.yandex.practicum.filmorate.dto.response;

import lombok.Value;

@Value
public class EventResponse {
    Long eventId;
    Long timestamp;
    Long userId;
    String eventType;
    String operation;
    Long entityId;
}
