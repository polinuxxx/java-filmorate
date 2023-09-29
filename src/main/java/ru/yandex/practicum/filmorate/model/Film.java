package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * Фильм.
 */
@Data
@Builder(toBuilder = true)
public class Film extends AbstractEntity {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной")
    private Integer duration;

    @AssertFalse(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public boolean isReleaseDateLessMinPossibleDate() {
        return releaseDate != null && releaseDate.isBefore(MIN_RELEASE_DATE);
    }
}
