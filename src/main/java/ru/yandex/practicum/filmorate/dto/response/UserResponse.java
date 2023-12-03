package ru.yandex.practicum.filmorate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

/**
 * Параметры ответа для {@link User}
 */
@Getter
@Setter
@Schema(description = "Параметры ответа для пользователя")
public class UserResponse {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Электронная почта", example = "user@mail.ru")
    private String email;

    @Schema(description = "Логин", example = "user")
    private String login;

    @Schema(description = "Имя", example = "Иван")
    private String name;

    @Schema(description = "Дата рождения", example = "1980-01-01")
    private LocalDate birthday;
}
