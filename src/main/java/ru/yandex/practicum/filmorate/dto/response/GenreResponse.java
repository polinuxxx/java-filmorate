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
@Schema(description = "Получение жанра")
public class GenreResponse {

    @Schema(description = "Идентификатор жанра", example = "1")
    private Long id;

    @Schema(description = "Название жанра", example = "Комедия")
    private String name;

}
