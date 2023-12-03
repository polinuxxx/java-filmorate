package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.event.Event;

import java.util.List;

/**
 * Интерфейс для хранилища {@link Event}.
 */
public interface EventStorage extends Storage<Event> {

    List<Event> getEventsByUserid(Long userId, int count);
}
