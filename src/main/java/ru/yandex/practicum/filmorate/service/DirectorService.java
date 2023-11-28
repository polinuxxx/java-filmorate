package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<Director> getAll() {
        List<Director> directors = directorStorage.getAll();
        log.debug("Получение всех режиссеров, текущее количество: {}", directors.size());
        return directors;
    }

    public Director getById(Long directorId) {
        log.debug("Получение режиссера по id = {}", directorId);

        exists(directorId);

        return directorStorage.getById(directorId);
    }

    public Director create(Director director) {
        log.debug("Добавление режиссера {}", director);

        validate(director);

        return directorStorage.create(director);
    }

    public Director update(Director director) {
        log.debug("Редактирование режиссера {}", director);

        validate(director);
        exists(director.getId());

        return directorStorage.update(director);
    }

    public void delete(Long directorId) {
        log.debug("Удаление режиссера с id = {}", directorId);
        exists(directorId);
        directorStorage.delete(directorId);
    }

    public void exists(Long id) {
        log.debug("Проверка режиссера на существование");

        if (id != null && !directorStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден режисер по id = %d.", id));
        }
    }

    private void validate(Director director) {
        if (director == null) {
            throw new ValidationException("Передан пустой объект режиссера.");
        }
    }
}