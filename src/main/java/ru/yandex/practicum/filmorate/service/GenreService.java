package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
        log.debug("Добавление жанра {}", genre);

        validate(genre);

        return genreStorage.create(genre);
    }

    public Genre update(Genre genre) {
        log.debug("Редактирование жанра {}", genre);

        validate(genre);

        return genreStorage.update(genre);
    }

    public void delete(Long id) {
        log.debug("Удаление жанра с id = {}", id);

        exists(id);

        genreStorage.delete(id);
    }

    public void exists(Long id) {
        log.debug("Проверка жанра на существование");

        if (id != null && !genreStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден жанр по id = %d.", id));
        }
    }

    private void validate(Genre genre) {
        if (genre == null) {
            throw new ValidationException("Передан пустой объект жанра.");
        }

        exists(genre.getId());
    }
}
