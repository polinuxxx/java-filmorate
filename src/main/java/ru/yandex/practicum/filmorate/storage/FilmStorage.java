package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * Интерфейс для хранилища {@link Film}.
 */
public interface FilmStorage extends Storage<Film> {

    List<Film> getPopular(int count);

    List<Film> getFilmsByQueryAndType(String query, String by);

    List<Film> getFilmsByDirector(Long directorId, String sortBy);

    List<Film> getRecommendationFilms(Long userId);

    List<Film> getPopular(int count, Integer genreId, Integer year);
}
