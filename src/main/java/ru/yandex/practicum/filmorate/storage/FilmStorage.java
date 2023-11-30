package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для хранилища {@link Film}.
 */
public interface FilmStorage extends Storage<Film> {

    List<Film> getFilmsByDirector(Long directorId, String sortBy);

    List<Film> getRecommendationFilms(Long userId);

    List<Film> getPopular(int count, Integer genreId, Integer year);
}
