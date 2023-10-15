package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

/**
 * Контроллер для {@link User}
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractController<User> {

    /**
     * Получение всех пользователей.
     */
    @GetMapping
    @Override
    public List<User> getAll() {
        log.debug("Получение всех пользователей, текущее количество: {}", items.size());

        return super.getAll();
    }

    /**
     * Создание пользователя.
     */
    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.debug("Добавление пользователя {}", user);

        return super.create(user);
    }

    /**
     * Редактирование пользователя.
     */
    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.debug("Редактирование пользователя {}", user);

        return super.update(user);
    }

    @Override
    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
