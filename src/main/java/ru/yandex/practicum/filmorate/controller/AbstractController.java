package ru.yandex.practicum.filmorate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

/**
 * Базовый контроллер.
 */
public abstract class AbstractController<T extends AbstractEntity> {

    private long counter;

    protected final Map<Long, T> items = new HashMap<>();

    public List<T> getAll() {
        return new ArrayList<>(items.values());
    }

    public T create(T item) {
        item.setId(++counter);

        validate(item);
        items.put(item.getId(), item);

        return item;
    }

    public T update(T item) {

        if (!items.containsKey(item.getId())) {
            throw new EntityNotFoundException(String.format("Сущность %s не найдена", item));
        }
        validate(item);
        items.put(item.getId(), item);

        return item;
    }

    public abstract void validate(T item);
}
