package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Класс-хранилище для  {@link Film}.
 */
@Component
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {
}
