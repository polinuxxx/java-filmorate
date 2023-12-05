package ru.yandex.practicum.filmorate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Параметры запроса для создания оценки {@link Film}.
 */
@Getter
@Setter
@Schema(description = "Создание оценки на фильм")
public class MarkCreateRequest {

    @Range(min = 1, max = 10, message = "Допустимая оценка от 1 до 10 включительно")
    private Integer mark;
}
