package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.List;

/**
 * Сервис для {@link Event}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventStorage eventStorage;

    /**
     * Добавление события пользователя.
     *
     * @param event - событие пользователя
     * @return - событие пользователя, записанное в хранилище
     */
    public Event add(Event event) {
        return eventStorage.create(event);
    }

    /**
     * Получение списка событий по идентификатору пользователя.
     *
     * @param userId - идентификатор пользователя
     * @param count  - максимальное кол-во возвращаемых событий
     * @return - список событий пользователя
     */
    public List<Event> getEventsByUserid(Long userId, int count) {
        return eventStorage.getEventsByUserid(userId, count);
    }
}
