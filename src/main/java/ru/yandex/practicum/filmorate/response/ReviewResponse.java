package ru.yandex.practicum.filmorate.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Review;

/**
 * Параметры ответа для получения {@link Review}.
 */
@Getter
@Setter
@Schema(description = "Получение отзывов на фильмы")
public class ReviewResponse {

    @JsonProperty("reviewId")
    @Schema(description = "Идентификатор ревью", example = "1")
    private Long id;

    @Schema(description = "Содержание отзыва", example = "Отзыв полезен")
    private String content;

    @Schema(description = "Признак положительного отзыва", example = "true")
    private Boolean isPositive;

    @Schema(description = "Идентификатор пользователя", example = "2")
    private Long userId;

    @Schema(description = "Идентификатор фильма", example = "3")
    private Long filmId;

    @Schema(description = "Рейтинг полезности", example = "10")
    private Integer useful;
}
