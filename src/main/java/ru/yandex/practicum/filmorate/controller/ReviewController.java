package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.converter.ReviewConverter;
import ru.yandex.practicum.filmorate.model.Reaction;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.dto.request.ReviewCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.ReviewUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.ReviewResponse;
import ru.yandex.practicum.filmorate.service.ReviewService;

/**
 * Контроллер для {@link Review}.
 */
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Управление отзывами на фильмы")
public class ReviewController {

    private final ReviewService reviewService;

    private final ReviewConverter reviewConverter;

    /**
     * Получение отзыва по идентификатору.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение отзыва по идентификатору")
    public ReviewResponse getById(@PathVariable @Parameter(description = "Идентификатор отзыва") Long id) {
        return reviewConverter.convert(reviewService.getById(id));
    }

    /**
     * Получение отзывов по идентификатору фильма.
     */
    @GetMapping
    @Operation(summary = "Получение отзывов по идентификатору фильма")
    public List<ReviewResponse> get(
            @RequestParam(required = false) @Parameter(description = "Идентификатор фильма") Long filmId,
            @RequestParam(defaultValue = "10") @Parameter(description = "Количество отзывов") Integer count) {

        return reviewConverter.convert(reviewService.getByFilmId(filmId, count));
    }

    /**
     * Создание отзыва.
     */
    @PostMapping
    @Operation(summary = "Создание отзыва")
    public ReviewResponse create(@RequestBody @Valid ReviewCreateRequest request) {
        return reviewConverter.convert(reviewService.create(reviewConverter.convert(request)));
    }

    /**
     * Редактирование отзыва.
     */
    @PutMapping
    @Operation(summary = "Редактирование отзыва")
    public ReviewResponse update(@RequestBody @Valid ReviewUpdateRequest request) {
        return reviewConverter.convert(reviewService.update(reviewConverter.convert(request)));
    }

    /**
     * Удаление отзыва.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление отзыва")
    public void delete(@PathVariable @Parameter(description = "Идентификатор отзыва") Long id) {
        reviewService.delete(id);
    }

    /**
     * Добавление лайка отзыву.
     */
    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "Добавление лайка отзыву")
    public void addLike(@PathVariable @Parameter(description = "Идентификатор отзыва") Long id,
                        @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId) {
        reviewService.addLike(id, userId, Reaction.LIKE.getCode());
    }

    /**
     * Добавление дизлайка отзыву.
     */
    @PutMapping("/{id}/dislike/{userId}")
    @Operation(summary = "Добавление дизлайка отзыву")
    public void addDislike(@PathVariable @Parameter(description = "Идентификатор отзыва") Long id,
                        @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId) {
        reviewService.addLike(id, userId, Reaction.DISLIKE.getCode());
    }

    /**
     * Удаление лайка у отзыва.
     */
    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "Удаление лайка у отзыва")
    public void deleteLike(@PathVariable @Parameter(description = "Идентификатор отзыва") Long id,
                        @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId) {
        reviewService.deleteLike(id, userId, Reaction.LIKE.getCode());
    }

    /**
     * Удаление дизлайка у отзыва.
     */
    @DeleteMapping("/{id}/dislike/{userId}")
    @Operation(summary = "Удаление дизлайка у отзыва")
    public void deleteDislike(@PathVariable @Parameter(description = "Идентификатор отзыва") Long id,
                           @PathVariable @Parameter(description = "Идентификатор пользователя") Long userId) {
        reviewService.deleteLike(id, userId, Reaction.DISLIKE.getCode());
    }
}