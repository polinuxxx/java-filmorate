package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

/**
 * Абстрактный класс-хранилище.
 */
public abstract class AbstractStorage<T extends AbstractEntity> implements Storage<T> {

    private final Map<Long, T> items = new HashMap<>();

    @Override
    public T getById(Long id) {
        if (!exists(id)) {
            throw new EntityNotFoundException(String.format("Сущность по id = %d не найдена", id));
        }
        return items.get(id);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public T create(T item) {
        validate(item);
        if (exists(item.getId())) {
            throw new EntityAlreadyExistsException(String.format("Сущность %s уже существует", item));
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public T update(T item) {
        validate(item);
        if (!exists(item.getId())) {
            throw new EntityNotFoundException(String.format("Сущность по id = %d не найдена", item.getId()));
        }
        items.put(item.getId(), item);

        return item;
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }

    @Override
    public boolean exists(Long id) {
        return items.containsKey(id);
    }

    protected void validate(T item) {
        if (item.getId() == null) {
            throw new ValidationException("Идентификатор сущности = null");
        }
    }
}
