package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Отзыв.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Review extends AbstractEntity {

    private String content;

    private Boolean isPositive;

    private User user;

    private Film film;

    private Integer useful;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", getId());
        values.put("content", content);
        values.put("is_positive", isPositive);
        values.put("user_id", user.getId());
        values.put("film_id", film.getId());
        values.put("useful", 0);
        return values;
    }
}
