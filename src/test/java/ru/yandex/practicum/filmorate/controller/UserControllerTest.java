package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.User;

class UserControllerTest extends AbstractControllerTest<UserController, User> {

    @BeforeEach
    void init() {

        entity = User.builder()
                .email("polina.kazichkina@yandex.ru")
                .login("polinuxxx")
                .name("Полина")
                .build();

        controller = new UserController();
    }
}