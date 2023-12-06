package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.model.event.Operation;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

/**
 * Сервис для {@link User}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    private final FriendDbStorage friendStorage;

    private final EventService eventService;

    public List<User> getAll() {
        List<User> users = userStorage.getAll();
        log.debug("Получение всех пользователей, текущее количество: {}", users.size());

        return users;
    }

    public User getById(Long id) {
        log.debug("Получение пользователя по id = {}", id);

        exists(id);

        return userStorage.getById(id);
    }

    /**
     * Получаем список рекомендуемых фильмов.
     *
     * @param id пользователя.
     * @return список фильмов.
     */
    public List<Film> getRecommendedFilms(Long id) {
        log.debug("Получение рекомендации для пользователя с id = {}", id);

        exists(id);

        return filmStorage.getRecommendedFilms(id);
    }

    public User create(User user) {
        log.debug("Добавление пользователя {}", user);

        validate(user);

        return userStorage.create(user);
    }

    public User update(User user) {
        log.debug("Редактирование пользователя {}", user);

        validate(user);

        return userStorage.update(user);
    }

    public void delete(Long id) {
        log.debug("Удаление пользователя с id = {}", id);

        exists(id);

        userStorage.delete(id);
    }

    public void addFriend(Long userId, Long friendId) {
        log.debug("Добавление пользователю с id = {} друга с id = {}", userId, friendId);

        exists(userId);
        exists(friendId);

        friendStorage.addFriendToUser(userId, friendId);
        User user = userStorage.getById(userId);
        eventService.add(Event.builder()
                .user(user)
                .type(EventType.FRIEND)
                .operation(Operation.ADD)
                .entityId(friendId)
                .build());
    }

    public void deleteFriend(Long userId, Long friendId) {
        log.debug("Удаление у пользователя с id = {} друга с id = {}", userId, friendId);

        exists(userId);
        exists(friendId);

        friendStorage.deleteFriendFromUser(userId, friendId);
        User user = userStorage.getById(userId);
        eventService.add(Event.builder()
                .user(user)
                .type(EventType.FRIEND)
                .operation(Operation.REMOVE)
                .entityId(friendId)
                .build());
    }

    public List<User> getFriends(Long id) {
        log.debug("Получение списка друзей пользователя с id = {}", id);

        exists(id);

        return friendStorage.getByUserId(id);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        log.debug("Получение списка общих друзей пользователей с id = {} и id = {}", userId, friendId);

        exists(userId);
        exists(friendId);

        return friendStorage.getCommonFriends(userId, friendId);
    }

    public void exists(Long id) {
        log.debug("Проверка пользователя на существование");

        if (id != null && !userStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Не найден пользователь по id = %d.", id));
        }
    }

    public List<Event> getFeed(Long userId, int count) {
        log.debug("Получение последних событий друзей пользователя с id = {} и лимитом в {} записей", userId, count);

        exists(userId);

        return eventService.getEventsByUserid(userId, count);
    }

    private void validate(User user) {
        if (user == null) {
            throw new ValidationException("Передан пустой объект пользователя.");
        }

        exists(user.getId());
    }
}
