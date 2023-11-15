package ru.yandex.practicum.filmorate.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Рейтинг MPA.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class RatingMpa extends AbstractEntity {

    private String name;

    private String description;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", getId());
        values.put("name", name);
        values.put("description", description);

        return values;
    }
}
