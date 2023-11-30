package ru.yandex.practicum.filmorate.service;

import java.util.List;

import io.swagger.v3.oas.models.security.SecurityScheme;
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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import java.util.Optional;

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

    private final DirectorDbStorage directorDbStorage;

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    private final RatingMpaStorage ratingMpaStorage;

    private final FilmGenreDbStorage filmGenreStorage;

    private final FilmDirectorDbStorage filmDirectorDbStorage;

    private final EventService eventService;

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

        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            filmDirectorDbStorage.addDirectorToFilm(createdFilm.getId(), film.getDirectors());
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

        filmDirectorDbStorage.deleteDirectorsFromFilm(film.getId());
        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            filmDirectorDbStorage.addDirectorToFilm(film.getId(), film.getDirectors());
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
        User user = userStorage.getById(userId);
        eventService.add(Event.builder()
                .user(user)
                .type(EventType.LIKE)
                .operation(Operation.ADD)
                .entityId(filmId)
                .build());
    }

    public void deleteLike(Long filmId, Long userId) {
        log.debug("Удаление лайка у фильма с id = {} пользователем {}", filmId, userId);

        exists(filmId);
        checkUserExists(userId);

        likeStorage.deleteLikeFromFilm(filmId, userId);
        User user = userStorage.getById(userId);
        eventService.add(Event.builder()
                .user(user)
                .type(EventType.LIKE)
                .operation(Operation.REMOVE)
                .entityId(filmId)
                .build());
    }

    public List<Film> getPopular(int count, Integer genreId, Integer year) {
        String logLineBuilder = "Получение самых популярных фильмов по количеству лайков." + " Айди жанра - "
                + (genreId != null ? genreId : "не указано") + "." +
                " Год выпуска фильма - " + (year != null ? year : "не указано");
        log.debug(logLineBuilder);
        return filmStorage.getPopular(count, genreId, year);
    }


    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        log.debug("Получение списка фильмов по режиссеру directorId={} с сортировкой по {}", directorId, sortBy);

        checkDirectorExists(directorId);

        return filmStorage.getFilmsByDirector(directorId, sortBy);
    }

    public void exists(Long id) {
        log.debug("Проверка фильма на существование");

        if (id != null && !filmStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден фильм по id = %d.", id));
        }
    }

    public List<Film> searchByQueryAndType(String query, String by) {
        log.debug("Поиск фильма. Запрос на поиск: {}. Поля для поиска: {} ", query, by);

        if (by == null || by.isBlank()) {
            throw new ParamNotExistException("Пустой фильтра поиска. Параметр by должен содержать тип поиска.");
        }

        if (query == null || query.isBlank()) {
            throw new SearchQueryException("Пустая строка для поиска. Параметр query должен быть заполнен.");
        }

        return filmStorage.getFilmsByQueryAndType(query, by);
    }

    private void validate(Film film) {
        if (film == null) {
            throw new ValidationException("Передан пустой объект фильма.");
        }

        exists(film.getId());
        film.getGenres().forEach(genre -> checkGenreExists(genre.getId()));
        film.getDirectors().forEach(director -> checkDirectorExists(director.getId()));
        checkMpaExists(film.getMpa().getId());
    }

    private void checkGenreExists(Long genreId) {
        if (genreId != null && !genreStorage.exists(genreId)) {
            throw new EntityNotFoundException(String.format("Не найден жанр по id = %d.", genreId));
        }
    }

    private void checkDirectorExists(Long directorId) {
        if (directorId != null && !directorDbStorage.exists(directorId)) {
            throw new EntityNotFoundException(String.format("Не найден режиссер по id = %d.", directorId));
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
