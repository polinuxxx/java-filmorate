package ru.yandex.practicum.filmorate.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Параметры запроса для редактирования {@link Film}.
 */
@Getter
@Setter
@Schema(description = "Обновление фильма")
public class FilmUpdateRequest extends FilmCreateRequest {

    @NotNull(message = "Идентификатор фильма не может быть пустым")
    @Schema(description = "Идентификатор фильма", example = "1")
    private Long id;
}
