package ru.yandex.practicum.filmorate.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Жанр.
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class Authority {

    private UUID id;
    private Long user_id;
    private String authority;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", id);
        values.put("user_id", user_id);
        values.put("authority", authority);

        return values;
    }
}
