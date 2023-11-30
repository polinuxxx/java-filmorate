package ru.yandex.practicum.filmorate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * Параметры запроса для создания {@link User}
 */
@Getter
@Setter
@Schema(description = "Параметры запроса для создания пользователя")
public class UserCreateRequest {

    @Email(message = "Электронная почта не соответствует формату")
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Schema(description = "Электронная почта", example = "user@mail.ru")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S*", message = "Логин не может содержать пробелы")
    @Schema(description = "Логин", example = "user")
    private String login;

    @Setter(AccessLevel.NONE)
    @Schema(description = "Имя", example = "Иван")
    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @Schema(description = "Дата рождения", example = "1980-01-01")
    private LocalDate birthday;

    public void setName(String name) {
        if ((name == null) || (name.isBlank())) {
            this.name = login;
        } else {
            this.name = name;
        }
    }
}
