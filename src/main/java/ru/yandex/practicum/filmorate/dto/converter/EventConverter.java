package ru.yandex.practicum.filmorate.dto.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.filmorate.dto.response.EventResponse;
import ru.yandex.practicum.filmorate.model.event.Event;

import java.time.Instant;
import java.util.List;

/**
 * Конвертер для {@link Event}
 */
@Mapper(componentModel = "spring")
public interface EventConverter {

    @Mapping(source = "id", target = "eventId")
    @Mapping(source = "type", target = "eventType")
    @Mapping(source = "createdAt", target = "timestamp")
    @Mapping(source = "user.id", target = "userId")
    EventResponse convert(Event event);

    List<EventResponse> convert(List<Event> events);

    default Long fromInstant(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }
}