package ru.yandex.practicum.filmorate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.RatingMpa;

/**
 * Параметры ответа для получения {@link RatingMpa}.
 */
@Getter
@Setter
@Schema(description = "Получение рейтинга MPA")
public class RatingMpaResponse {

    @Schema(description = "Идентификатор рейтинга MPA", example = "1")
    private Long id;

    @Schema(description = "Название MPA рейтинга", example = "G")
    private String name;

    @Schema(description = "Описание MPA рейтинга", example = "У фильма нет возрастных ограничений")
    private String description;

}
