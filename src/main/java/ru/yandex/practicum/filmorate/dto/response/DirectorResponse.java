package ru.yandex.practicum.filmorate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Director;

/**
 * Параметры ответа для получения {@link Director}.
 */
@Getter
@Setter
@Schema(description = "Получение режиссера")
public class DirectorResponse {

    @Schema(description = "Идентификатор режиссера", example = "1")
    private Long id;

    @Schema(description = "ФИО режиссера", example = "Джордж Лукас")
    private String name;

}
