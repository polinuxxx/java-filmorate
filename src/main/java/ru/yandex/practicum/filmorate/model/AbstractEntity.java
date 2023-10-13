package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Базовая сущность.
 */
@Getter
@Setter
public class AbstractEntity {

    @EqualsAndHashCode.Exclude
    protected Integer id;
}
