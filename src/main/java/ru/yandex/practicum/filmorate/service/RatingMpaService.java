package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
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
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    public RatingMpa update(RatingMpa ratingMpa) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException("Операция не разрешена");
    }

    public void exists(Long id) {
        log.debug("Проверка рейтинга MPA на существование");

        if (id != null && !ratingMpaStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден рейтинг MPA по id = %d.", id));
        }
    }
}
