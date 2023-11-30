package ru.yandex.practicum.filmorate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Review;

/**
 * Параметры запроса для создания {@link Review}.
 */
@Getter
@Setter
@Schema(description = "Создание отзыва на фильм")
public class ReviewCreateRequest {

    @NotBlank(message = "Содержание отзыва не может быть пустым")
    @Schema(description = "Содержание отзыва", example = "Отзыв полезен")
    private String content;

    @NotNull(message = "Признак положительного отзыва не может быть пустым")
    @Schema(description = "Признак положительного отзыва", example = "true")
    private Boolean isPositive;

    @NotNull(message = "Идентификатор пользователя не может быть пустым")
    @Schema(description = "Идентификатор пользователя", example = "2")
    private Long userId;

    @NotNull(message = "Идентификатор фильма не может быть пустым")
    @Schema(description = "Идентификатор фильма", example = "3")
    private Long filmId;
}
