package ru.yandex.practicum.filmorate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Director;

/**
 * Параметры запроса для создания {@link Director}.
 */
@Getter
@Setter
@Schema(description = "Обновление режиссера")
public class DirectorUpdateRequest extends DirectorCreateRequest {

    @NotNull(message = "Идентификатор режиссера не может быть пустым")
    @Schema(description = "Идентификатор режиссера", example = "1")
    private Long id;

}
