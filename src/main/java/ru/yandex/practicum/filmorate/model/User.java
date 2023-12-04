package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Пользователь.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class User extends AbstractEntity {

    private String email;

    private String login;

    @Builder.Default
    private String password = "123";

    @Builder.Default
    private boolean enabled = true;

    private String name;

    private LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("id", getId());
        values.put("email", email);
        values.put("login", login);
        values.put("password", password);
        values.put("name", name);
        values.put("enabled", enabled);
        values.put("birthday", birthday);
        return values;
    }
}
