package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер для {@link Film}.
 */
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    /**
     * Получение всех фильмов.
     */
    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    /**
     * Получение фильма по идентификатору.
     */
    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        return filmService.getById(id);
    }

    /**
     * Создание фильма.
     */
    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    /**
     * Редактирование фильма.
     */
    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        return filmService.update(film);
    }

    /**
     * Удаление фильма.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        filmService.delete(id);
    }

    /**
     * Добавление фильму лайка.
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    /**
     * Удаление у фильма лайка.
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    /**
     * Получение наиболее популярных фильмов по количеству лайков
     * Опционально: в конкретном жанре или выпущенных в определенном году.
     */
    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count,
                                 @RequestParam(required = false) Optional<Integer> genreId,
                                 @RequestParam(required = false) Optional<Integer> year) {
        return filmService.getPopular(count,genreId,year);
    }
}
