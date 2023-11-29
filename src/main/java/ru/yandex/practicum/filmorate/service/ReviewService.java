package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Reaction;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

/**
 * Сервис для {@link Review}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewStorage reviewStorage;

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    private final ReviewLikeDbStorage reviewLikeStorage;

    public List<Review> getAll() {
        List<Review> reviews = reviewStorage.getAll();
        log.debug("Получение всех отзывов, текущее количество: {}", reviews.size());

        return reviews;
    }

    public Review getById(Long id) {
        log.debug("Получение отзыва по id = {}", id);

        exists(id);

        return reviewStorage.getById(id);
    }

    public Review create(Review review) {
        log.debug("Добавление отзыва {}", review);

        validate(review);

        return reviewStorage.create(review);
    }

    public Review update(Review review) {
        log.debug("Редактирование отзыва {}", review);

        validate(review);

        return reviewStorage.update(review);
    }

    public void delete(Long id) {
        log.debug("Удаление отзыва с id = {}", id);

        exists(id);

        reviewStorage.delete(id);
    }

    public void addLike(Long reviewId, Long userId, Reaction reaction) {
        log.debug("Добавление лайка/дизлайка отзыву с id = {} пользователем {}", reviewId, userId);

        exists(reviewId);
        checkUserExists(userId);

        reviewLikeStorage.addLikeToReview(reviewId, userId, reaction.getCode());
        reviewStorage.recalculateUseful(reviewId, reaction.getCode());
    }

    public void deleteLike(Long reviewId, Long userId, Reaction reaction) {
        log.debug("Удаление лайка/дизлайка у отзыва с id = {} пользователем {}", reviewId, userId);

        exists(reviewId);
        checkUserExists(userId);

        reviewLikeStorage.deleteLikeFromReview(reviewId, userId, reaction.getCode());
        reviewStorage.recalculateUseful(reviewId, reaction.getCode());
    }

    public List<Review> getByFilmId(Long filmId, Integer count) {
        log.debug("Получение отзывов по идентификатору фильма = {}", filmId);

        checkFilmExists(filmId);

        return reviewStorage.getByFilmId(filmId, count);
    }

    public void exists(Long id) {
        log.debug("Проверка отзыва на существование");

        if (id != null && !reviewStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден отзыв по id = %d.", id));
        }
    }

    private void validate(Review review) {
        if (review == null) {
            throw new ValidationException("Передан пустой объект отзыва.");
        }

        exists(review.getId());
        checkFilmExists(review.getFilm().getId());
        checkUserExists(review.getUser().getId());
    }

    private void checkFilmExists(Long filmId) {
        if (filmId != null && !filmStorage.exists(filmId)) {
            throw new EntityNotFoundException(String.format("Не найден фильм по id = %d.", filmId));
        }
    }

    private void checkUserExists(Long userId) {
        if (userId != null && !userStorage.exists(userId)) {
            throw new EntityNotFoundException(String.format("Не найден пользователь по id = %d.", userId));
        }
    }
}
