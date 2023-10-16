package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

/**
 * Базовый интерфейс для хранилища.
 */
public interface Storage<T extends AbstractEntity> {
    T getById(Long id);

    List<T> getAll();

    T create(T item);

    T update(T item);

    void delete(Long id);

    boolean exists(Long id);
}
