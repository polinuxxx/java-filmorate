package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import javax.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.converter.UserConverter;
import ru.yandex.practicum.filmorate.dto.request.UserCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.UserUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.UserResponse;
import ru.yandex.practicum.filmorate.dto.converter.FilmConverter;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;
import ru.yandex.practicum.filmorate.dto.converter.EventConverter;
import ru.yandex.practicum.filmorate.dto.response.EventResponse;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

/**
 * Контроллер для {@link User}
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    private final EventConverter eventConverter;

    private final FilmConverter filmConverter;

    private final UserConverter userConverter;

    /**
     * Получение всех пользователей.
     * @return список пользователей
     */
    @GetMapping
    @Operation(summary = "Получение всех пользователей")
    public List<UserResponse> getAll() {
        return userConverter.convert(userService.getAll());
    }

    /**
     * Получение пользователя по идентификатору.
     * @return пользователь
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по идентификатору")
    public UserResponse getById(@PathVariable @Parameter(description = "Идентификатор пользователя") Long id) {
        return userConverter.convert(userService.getById(id));
    }

    /**
     * Получение пользователя по логину.
     * @return пользователь
     */
    @GetMapping("/login/{login}")
    @Operation(summary = "Получение пользователя по логину")
    public UserResponse getByLogin(@PathVariable @Parameter(description = "Логин пользователя") String login) {
        return userConverter.convert(userService.getByLogin(login));
    }

    /**
     * Получение рекомендуемых фильмов.
     * @param id идентификатор пользователя
     * @return список рекомендуемых фильмов
     */
    @GetMapping("/{id}/recommendations")
    @Operation(summary = "Получение рекомендуемых фильмов")
    public List<FilmResponse> getRecommendations(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long id) {
        return filmConverter.convert(userService.getRecommendationsFilms(id));
    }

    /**
     * Создание пользователя.
     * @param request параметры запроса
     * @return созданный пользователь
     */
    @PostMapping
    @Operation(summary = "Создание пользователя")
    public UserResponse create(@RequestBody @Valid UserCreateRequest request) {
        return userConverter.convert(userService.create(userConverter.convert(request)));
    }

    /**
     * Редактирование пользователя.
     * @param request параметры запроса
     * @return обновленный пользователь
     */
    @PutMapping
    @Operation(summary = "Редактирование пользователя")
    public UserResponse update(@RequestBody @Valid UserUpdateRequest request) {
        return userConverter.convert(userService.update(userConverter.convert(request)));
    }

    /**
     * Удаление пользователя.
     * @param id идентификатор пользователя
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя по идентификатору")
    public void delete(@PathVariable @Parameter(description = "Идентификатор пользователя") Long id) {
        userService.delete(id);
    }

    /**
     * Добавление друга пользователю.
     * @param id идентификатор пользователя
     * @param friendId идентификатор друга
     */
    @PutMapping("/{id}/friends/{friendId}")
    @Operation(summary = "Добавление друга пользователю")
    public void addFriend(@PathVariable @Parameter(description = "Идентификатор пользователя") Long id,
                          @PathVariable @Parameter(description = "Идентификатор друга") Long friendId) {
        userService.addFriend(id, friendId);
    }

    /**
     * Удаление друга у пользователя.
     * @param id идентификатор пользователя
     * @param friendId идентификатор друга
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    @Operation(summary = "Удаление друга у пользователя")
    public void deleteFriend(@PathVariable @Parameter(description = "Идентификатор пользователя") Long id,
                             @PathVariable @Parameter(description = "Идентификатор друга") Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    /**
     * Получение друзей пользователя по идентификатору.
     * @param id идентификатор пользователя
     * @return список друзей
     */
    @GetMapping("/{id}/friends")
    @Operation(summary = "Получение друзей пользователя по идентификатору")
    public List<UserResponse> getFriends(@PathVariable @Parameter(description = "Идентификатор пользователя") Long id) {
        return userConverter.convert(userService.getFriends(id));
    }

    /**
     * Получение общих друзей двух пользователей.
     * @param id идентификатор пользователя
     * @param otherId идентификатор друга
     * @return список общих друзей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    @Operation(summary = "Получение общих друзей двух пользователей")
    public List<UserResponse> getCommonFriends(
            @PathVariable @Parameter(description = "Идентификатор пользователя") Long id,
            @PathVariable @Parameter(description = "Идентификатор друга") Long otherId) {
        return userConverter.convert(userService.getCommonFriends(id, otherId));
    }

    /**
     * Получение ленты пользователя.
     * @param id идентификатор пользователя
     * @param count количество записей
     * @return список событий
     */
    @GetMapping("/{id}/feed")
    @Operation(summary = "Получение ленты пользователя")
    public List<EventResponse> getFeed(@PathVariable @Parameter(description = "Идентификатор пользователя") Long id,
                                       @RequestParam(defaultValue = "1000")
                                       @Parameter(description = "Количество записей") int count) {
        return eventConverter.convert(userService.getFeed(id, count));
    }
}
