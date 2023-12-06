package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
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

    private String name;

    private LocalDate birthday;
}
