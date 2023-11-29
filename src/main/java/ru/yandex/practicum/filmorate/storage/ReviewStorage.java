package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

/**
 * Интерфейс для хранилища {@link Review}.
 */
public interface ReviewStorage extends Storage<Review> {
    void recalculateUseful(Long id, Integer reaction);

    List<Review> getByFilmId(Long filmId, Integer count);
}
