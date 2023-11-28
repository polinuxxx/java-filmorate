package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Типы реакций (лайк/дизлайк)
 */
@RequiredArgsConstructor
@Getter
public enum Reaction {
    LIKE(1),
    DISLIKE(-1);

    private final int code;
}
