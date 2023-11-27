package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.User;

/**
 * Интерфейс для хранилища {@link User}.
 */
public interface UserStorage extends Storage<User> {
    List<Long> getRecommendationsFilmIDs(Long id);

}
