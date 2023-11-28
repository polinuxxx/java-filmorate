package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
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

    public List<Film> getPopular(int count, Optional<Integer> genreId, Optional<Integer> year) {
        StringBuilder logLineBuilder = new StringBuilder("Получение самых популярных фильмов по количеству лайков.");
        genreId.ifPresent(id -> logLineBuilder.append(" Айди жанра - ").append(id).append("."));
        year.ifPresent(y -> logLineBuilder.append(" Год выпуска фильма - ").append(y));
        log.debug(logLineBuilder.toString());
        return filmStorage.getPopular(count, genreId,year);
    }

    public void exists(Long id) {
        log.debug("Проверка фильма на существование");

        if (id != null && !filmStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден фильм по id = %d.", id));
        }
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
