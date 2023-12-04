package ru.yandex.practicum.filmorate.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

/**
 * Параметры запроса для создания {@link Film}.
 */
@Getter
@Setter
@Schema(description = "Создание фильма")
public class FilmCreateRequest {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @NotBlank(message = "Название не может быть пустым")
    @Schema(description = "Название фильма", example = "Аватар")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    @Schema(description = "Описание фильма", example = "Про синих человекав!")
    private String description;

    @Schema(description = "Дата релиза", example = "2023-11-30")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной")
    @Schema(description = "Продолжительность фильма в минутах", example = "300")
    private Integer duration;

    @Schema(description = "Жанры")
    private Set<Genre> genres = new HashSet<>();

    @Schema(description = "Режиссеры")
    private Set<Director> directors = new HashSet<>();

    @NotNull(message = "Рейтинг MPA не может быть пустой")
    @Schema(description = "Рейтинг MPA")
    private RatingMpa mpa;

    @JsonIgnore
    @AssertFalse(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public boolean isReleaseDateLessMinPossibleDate() {
        return releaseDate != null && releaseDate.isBefore(MIN_RELEASE_DATE);
    }
}
