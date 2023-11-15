package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

/**
 * Сервис для {@link RatingMpa}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RatingMpaService {

    private final RatingMpaStorage ratingMpaStorage;

    public List<RatingMpa> getAll() {
        log.debug("Получение всех рейтингов MPA");

        return ratingMpaStorage.getAll();
    }

    public RatingMpa getById(Long id) {
        log.debug("Получение рейтинга MPA по id = {}", id);

        exists(id);

        return ratingMpaStorage.getById(id);
    }

    public RatingMpa create(RatingMpa ratingMpa) {
        log.debug("Добавление рейтинга MPA {}", ratingMpa);

        validate(ratingMpa);

        return ratingMpaStorage.create(ratingMpa);
    }

    public RatingMpa update(RatingMpa ratingMpa) {
        log.debug("Редактирование рейтинга MPA {}", ratingMpa);

        validate(ratingMpa);

        return ratingMpaStorage.update(ratingMpa);
    }

    public void delete(Long id) {
        log.debug("Удаление рейтинга MPA с id = {}", id);

        exists(id);

        ratingMpaStorage.delete(id);
    }

    public void exists(Long id) {
        log.debug("Проверка рейтинга MPA на существование");

        if (id != null && !ratingMpaStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден рейтинг MPA по id = %d.", id));
        }
    }

    private void validate(RatingMpa ratingMpa) {
        if (ratingMpa == null) {
            throw new ValidationException("Передан пустой объект рейтинга MPA.");
        }

        exists(ratingMpa.getId());
    }
}
