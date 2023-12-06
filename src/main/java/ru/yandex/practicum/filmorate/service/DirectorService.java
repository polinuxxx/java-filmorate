package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

/**
 * Сервис для {@link Director}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorService {

    private final DirectorStorage directorStorage;

    /**
     * Получение списка режиссеров
     */
    public List<Director> getAll() {
        List<Director> directors = directorStorage.getAll();
        log.debug("Получение всех режиссеров, текущее количество: {}", directors.size());
        return directors;
    }

    /**
     * Получение режиссера по идентификатору
     *
     * @param directorId - идентификатор режиссера
     */
    public Director getById(Long directorId) {
        log.debug("Получение режиссера по id = {}", directorId);

        exists(directorId);

        return directorStorage.getById(directorId);
    }

    /**
     * Создание режиссера
     */
    public Director create(Director director) {
        log.debug("Добавление режиссера {}", director);

        validate(director);

        return directorStorage.create(director);
    }

    /**
     * Обновление режиссера
     */
    public Director update(Director director) {
        log.debug("Редактирование режиссера {}", director);

        validate(director);
        exists(director.getId());

        return directorStorage.update(director);
    }

    /**
     * Удаление режиссера по идентификатору
     *
     * @param directorId - идентификатор режиссера
     */
    public void delete(Long directorId) {
        log.debug("Удаление режиссера с id = {}", directorId);
        exists(directorId);
        directorStorage.delete(directorId);
    }

    /**
     * Проверка режиссера на существование
     *
     * @param id - идентификатор режиссера
     */
    public void exists(Long id) {
        log.debug("Проверка режиссера на существование");

        if (id != null && !directorStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден режиссер по id = %d.", id));
        }
    }

    /**
     * Валидация режиссера
     */
    private void validate(Director director) {
        if (director == null) {
            throw new ValidationException("Передан пустой объект режиссера.");
        }
    }
}