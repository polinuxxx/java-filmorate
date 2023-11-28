package ru.yandex.practicum.filmorate.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Review;

/**
 * Параметры запроса для редактирования {@link Review}.
 */
@Getter
@Setter
@Schema(description = "Редактирование отзыва на фильм")
public class ReviewUpdateRequest extends ReviewCreateRequest {

    @JsonProperty("reviewId")
    @NotNull(message = "Идентификатор отзыва не может быть пустым")
    @Schema(description = "Идентификатор ревью", example = "1")
    private Long id;
}
