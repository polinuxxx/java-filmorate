package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Фильм.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Film extends AbstractEntity {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной")
    private Integer duration;

    private Set<Genre> genres = new HashSet<>();

    @NotNull(message = "Рейтинг MPA не может быть пустой")
    private RatingMpa mpa;

    @JsonIgnore
    @AssertFalse(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public boolean isReleaseDateLessMinPossibleDate() {
        return releaseDate != null && releaseDate.isBefore(MIN_RELEASE_DATE);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", getId());
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration_min", duration);
        values.put("rating_mpa_id", mpa.getId());

        return values;
    }
}
