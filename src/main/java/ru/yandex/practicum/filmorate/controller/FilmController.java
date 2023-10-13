package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Контроллер для {@link Film}.
 */
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractController<Film> {

    /**
     * Получение всех фильмов.
     */
    @GetMapping
    public List<Film> getAll() {
        log.debug("Получение всех фильмов, текущее количество: {}", items.size());

        return super.getAll();
    }

    /**
     * Создание фильма.
     */
    @PostMapping
    public Film create(@RequestBody @Valid Film film) {

        log.debug("Добавление фильма {}", film);

        return super.create(film);
    }

    /**
     * Редактирование фильма.
     */
    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.debug("Редактирование фильма {}", film);

        return super.update(film);
    }
}
