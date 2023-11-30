package ru.yandex.practicum.filmorate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Director;

/**
 * Параметры запроса для создания {@link Director}.
 */
@Getter
@Setter
@Schema(description = "Создание режиссера")
public class DirectorCreateRequest {

    @NotBlank(message = "Имя не должно быть пустым")
    @Schema(description = "ФИО режиссера", example = "Джордж Лукас")
    private String name;

}
