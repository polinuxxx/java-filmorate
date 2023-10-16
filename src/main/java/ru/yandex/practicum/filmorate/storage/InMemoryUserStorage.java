package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

/**
 * Класс-хранилище для {@link User}.
 */
@Component
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {
}
