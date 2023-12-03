package ru.yandex.practicum.filmorate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Параметры ответа для получения {@link Film}.
 */
@Getter
@Setter
@Schema(description = "Получение фильма")
public class FilmResponse {


    @Schema(description = "Идентификатор фильма", example = "1")
    private Long id;

    @Schema(description = "Название фильма", example = "Аватар")
    private String name;

    @Schema(description = "Описание фильма", example = "Про синих человекав!")
    private String description;

    @Schema(description = "Дата релиза", example = "2023-11-30")
    private LocalDate releaseDate;

    @Schema(description = "Продолжительность фильма в минутах", example = "300")
    private Integer duration;

    @Schema(description = "Жанры")
    private Set<GenreResponse> genres;

    @Schema(description = "Режиссеры")
    private Set<DirectorResponse> directors;

    @Schema(description = "Рейтинг MPA")
    private RatingMpaResponse mpa;

}
