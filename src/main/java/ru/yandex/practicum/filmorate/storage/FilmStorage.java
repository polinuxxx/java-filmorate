package ru.yandex.practicum.filmorate.storage;

import java.util.HashSet;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * Интерфейс для хранилища {@link Film}.
 */
public interface FilmStorage extends Storage<Film> {

    List<Film> getPopular(int count);

    List<Film> search(String query, HashSet<String> by);

    List<Film> getFilmsByDirector(Long directorId, String sortBy);

    List<Film> getRecommendationFilms(Long userId);

}
