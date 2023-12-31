package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    @Builder.Default
    private Set<Director> directors = new HashSet<>();

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
