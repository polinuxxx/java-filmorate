package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import javax.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

/**
 * Пользователь.
 */
@Data
@Builder(toBuilder = true)
public class User extends AbstractEntity {

    @Email(message = "Электронная почта не соответствует формату")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S*", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
