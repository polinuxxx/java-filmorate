package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractControllerTest<T extends AbstractController, E extends AbstractEntity> {

    protected T controller;

    protected E entity;

    @Test
    void shouldThrowValidationExceptionWhenEntityAlreadyExists() {

        controller.create(entity);

        ValidationException exception = assertThrows(
                ValidationException.class, () -> controller.create(entity)
        );
        assertEquals(entity.getClass().getName() + " уже создан", exception.getMessage());
    }
}