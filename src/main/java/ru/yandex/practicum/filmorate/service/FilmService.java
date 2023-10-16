package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

/**
 * Сервис для {@link Film}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private long counter;

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        log.debug("Получение всех фильмов, текущее количество: {}", films.size());

        return films;
    }

    public Film getById(Long id) {
        log.debug("Получение фильма по id = {}", id);

        return filmStorage.getById(id);
    }

    public Film create(Film film) {
        log.debug("Добавление фильма {}", film);

        film.setId(++counter);

        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.debug("Редактирование фильма {}", film);

        return filmStorage.update(film);
    }

    public void delete(Long id) {
        log.debug("Удаление фильма с id = {}", id);

        filmStorage.delete(id);
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("Добавление лайка фильму с id = {} пользователем {}", filmId, userId);

        checkParams(filmId, userId);

        Set<Long> likes = filmStorage.getById(filmId).getLikes();
        likes.add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        log.debug("Удаление лайка у фильма с id = {} пользователем {}", filmId, userId);

        checkParams(filmId, userId);

        Set<Long> likes = filmStorage.getById(filmId).getLikes();
        likes.remove(userId);
    }

    public List<Film> getPopular(int count) {
        log.debug("Получение {} самых популярных фильмов по количеству лайков", count);

        return filmStorage.getAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }

    private void checkParams(Long filmId, Long userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
    }

    private void checkFilmExists(Long id) {
        if (id != null && !filmStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Фильм с id = %s не найден", id));
        }
    }

    private void checkUserExists(Long id) {
        if (id != null && !userStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Пользователь с id = %s не найден", id));
        }
    }
}
