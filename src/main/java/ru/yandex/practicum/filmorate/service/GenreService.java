package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

/**
 * Сервис для {@link Genre}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> getAll() {
        log.debug("Получение всех жанров");

        return genreStorage.getAll();
    }

    public Genre getById(Long id) {
        log.debug("Получение жанра по id = {}", id);

        exists(id);

        return genreStorage.getById(id);
    }

    public Genre create(Genre genre) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    public Genre update(Genre genre) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    public void exists(Long id) {
        log.debug("Проверка жанра на существование");

        if (id != null && !genreStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден жанр по id = %d.", id));
        }
    }
}
