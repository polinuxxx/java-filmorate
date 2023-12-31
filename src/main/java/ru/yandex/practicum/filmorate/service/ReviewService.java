package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Reaction;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

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

    private final EventService eventService;

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

        review = reviewStorage.create(review);
        eventService.add(Event.builder()
                .user(review.getUser())
                .type(EventType.REVIEW)
                .operation(Operation.ADD)
                .entityId(review.getId()).build());
        return review;
    }

    public Review update(Review review) {
        log.debug("Редактирование отзыва {}", review);

        validate(review);

        review = reviewStorage.update(review);
        eventService.add(Event.builder()
                .user(review.getUser())
                .type(EventType.REVIEW)
                .operation(Operation.UPDATE)
                .entityId(review.getId()).build());
        return review;
    }

    public void delete(Long id) {
        log.debug("Удаление отзыва с id = {}", id);

        exists(id);

        Review review = reviewStorage.getById(id);
        reviewStorage.delete(id);
        eventService.add(Event.builder()
                .user(review.getUser())
                .type(EventType.REVIEW)
                .operation(Operation.REMOVE)
                .entityId(review.getId()).build());
    }

    public void addReaction(Long reviewId, Long userId, Reaction reaction) {
        log.debug("Добавление лайка/дизлайка отзыву с id = {} пользователем {}", reviewId, userId);

        exists(reviewId);
        checkUserExists(userId);

        reviewLikeStorage.addReactionToReview(reviewId, userId, reaction.getCode());
        reviewStorage.recalculateUseful(reviewId, reaction.getCode());
    }

    public void deleteReaction(Long reviewId, Long userId, Reaction reaction) {
        log.debug("Удаление лайка/дизлайка у отзыва с id = {} пользователем {}", reviewId, userId);

        exists(reviewId);
        checkUserExists(userId);

        reviewLikeStorage.deleteReactionFromReview(reviewId, userId, reaction.getCode());
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
