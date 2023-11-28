package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для хранилища {@link Film}.
 */
public interface FilmStorage extends Storage<Film> {
    List<Film> getPopular(int count, Optional<Integer> genreId, Optional<Integer> year);
}
