package ru.yandex.practicum.filmorate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.User;

/**
 * Параметры запроса для редактирования {@link User}
 */
@Getter
@Setter
@Schema(description = "Параметры запроса для редактирования пользователя")
public class UserUpdateRequest extends UserCreateRequest {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;
}
