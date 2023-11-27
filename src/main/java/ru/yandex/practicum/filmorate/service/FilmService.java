package ru.yandex.practicum.filmorate.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ParamNotExistException;
import ru.yandex.practicum.filmorate.exception.SearchQueryException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

/**
 * Сервис для {@link Film}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    private final LikeDbStorage likeStorage;

    private final GenreStorage genreStorage;

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    private final RatingMpaStorage ratingMpaStorage;

    private final FilmGenreDbStorage filmGenreStorage;

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        log.debug("Получение всех фильмов, текущее количество: {}", films.size());

        return films;
    }

    public Film getById(Long id) {
        log.debug("Получение фильма по id = {}", id);

        exists(id);

        return filmStorage.getById(id);
    }

    @Transactional
    public Film create(Film film) {
        log.debug("Добавление фильма {}", film);

        validate(film);

        Film createdFilm = filmStorage.create(film);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmGenreStorage.addGenresToFilm(createdFilm.getId(), film.getGenres());
        }

        return getById(createdFilm.getId());
    }

    @Transactional
    public Film update(Film film) {
        log.debug("Редактирование фильма {}", film);

        validate(film);

        filmGenreStorage.deleteGenresFromFilm(film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmGenreStorage.addGenresToFilm(film.getId(), film.getGenres());
        }

        return filmStorage.update(film);
    }

    public void delete(Long id) {
        log.debug("Удаление фильма с id = {}", id);

        exists(id);

        filmStorage.delete(id);
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("Добавление лайка фильму с id = {} пользователем {}", filmId, userId);

        exists(filmId);
        checkUserExists(userId);

        likeStorage.addLikeToFilm(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        log.debug("Удаление лайка у фильма с id = {} пользователем {}", filmId, userId);

        exists(filmId);
        checkUserExists(userId);

        likeStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        log.debug("Получение {} самых популярных фильмов по количеству лайков", count);

        return filmStorage.getPopular(count);
    }

    public void exists(Long id) {
        log.debug("Проверка фильма на существование");

        if (id != null && !filmStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден фильм по id = %d.", id));
        }
    }

    public List<Film> search(String query, String by) {
        log.debug("Поиск фильмов");

        if (by == null || by.isEmpty()) {
            throw new ParamNotExistException("Пустой фильтра поиска. Параметр by должен содержать тип поиска.");
        }

        if (query == null || query.isEmpty()) {
            throw new SearchQueryException("Пустая строка для поиска. Параметр query должен быть заполнен.");
        }
        String[] params = by.split(",");
        HashSet<String> stringHashSet = new HashSet<>();
        stringHashSet.addAll(Arrays.asList(params));

        return filmStorage.search(query, stringHashSet);
    }

    private void validate(Film film) {
        if (film == null) {
            throw new ValidationException("Передан пустой объект фильма.");
        }

        exists(film.getId());
        film.getGenres().forEach(genre -> checkGenreExists(genre.getId()));
        checkMpaExists(film.getMpa().getId());
    }

    private void checkGenreExists(Long genreId) {
        if (genreId != null && !genreStorage.exists(genreId)) {
            throw new EntityNotFoundException(String.format("Не найден жанр по id = %d.", genreId));
        }
    }

    private void checkMpaExists(Long mpaId) {
        if (mpaId != null && !ratingMpaStorage.exists(mpaId)) {
            throw new EntityNotFoundException(String.format("Не найден рейтинг MPA по id = %d.", mpaId));
        }
    }

    private void checkUserExists(Long userId) {
        if (userId != null && !userStorage.exists(userId)) {
            throw new EntityNotFoundException(String.format("Не найден пользователь по id = %d.", userId));
        }
    }
}
