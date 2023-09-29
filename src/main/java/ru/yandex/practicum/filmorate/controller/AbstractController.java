package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовый контроллер.
 */
public abstract class AbstractController<T extends AbstractEntity> {

    private int counter = 0;

    protected final Map<Integer, T> items = new HashMap<>();

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

        validate(item);
        items.put(item.getId(), item);

        return item;
    }

    protected void validate(T item) {
        if (items.containsValue(item)) {
            throw new ValidationException(item.getClass().getName() + " уже создан");
        }
    }
}
