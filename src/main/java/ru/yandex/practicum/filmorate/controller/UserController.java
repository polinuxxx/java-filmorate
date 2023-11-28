package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

/**
 * Контроллер для {@link User}
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Получение всех пользователей.
     */
    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    /**
     * Получение пользователя по идентификатору.
     */
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * Получение рекомендуемых фильмов.
     */
    @GetMapping("/{id}/recommendations")
    public List<Film> getRecomendation(@PathVariable Long id) {
        return userService.getRecommendationsFilms(id);
    }

    /**
     * Создание пользователя.
     */
    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    /**
     * Редактирование пользователя.
     */
    @PutMapping
    public User update(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    /**
     * Удаление пользователя.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
