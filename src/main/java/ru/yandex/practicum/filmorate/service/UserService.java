package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

/**
 * Сервис для {@link User}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    private long counter;

    public List<User> getAll() {
        List<User> users = userStorage.getAll();
        log.debug("Получение всех пользователей, текущее количество: {}", users.size());

        return users;
    }

    public User getById(Long id) {
        log.debug("Получение пользователя по id = {}", id);

        return userStorage.getById(id);
    }

    public User create(User user) {
        log.debug("Добавление пользователя {}", user);

        user.setId(++counter);
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

        userStorage.delete(id);
    }

    public void addFriend(Long userId, Long friendId) {
        log.debug("Добавление пользователю с id = {} друга с id = {}", userId, friendId);

        checkUserExists(userId);
        checkUserExists(friendId);

        User user = userStorage.getById(userId);
        user.getFriends().add(friendId);

        User friend = userStorage.getById(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        log.debug("Удаление у пользователя с id = {} друга с id = {}", userId, friendId);

        checkUserExists(userId);
        checkUserExists(friendId);

        User user = userStorage.getById(userId);
        user.getFriends().remove(friendId);

        User friend = userStorage.getById(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long id) {
        log.debug("Получение списка друзей пользователя с id = {}", id);

        return userStorage.getById(id).getFriends().stream().map(userStorage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        log.debug("Получение списка общих друзей пользователей с id = {} и id = {}", userId, friendId);

        checkUserExists(userId);
        checkUserExists(friendId);

        if (userStorage.getById(userId).getFriends().isEmpty()
                || userStorage.getById(friendId).getFriends().isEmpty()) {
            return new ArrayList<>();
        }

        Set<Long> intersection = new HashSet<>(userStorage.getById(userId).getFriends());
        intersection.retainAll(userStorage.getById(friendId).getFriends());

        return intersection.stream().map(userStorage::getById).collect(Collectors.toList());
    }

    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void checkUserExists(Long id) {
        if (id != null && !userStorage.exists(id)) {
            throw new EntityNotFoundException(String.format("Пользователь с id = %s не найден", id));
        }
    }
}
