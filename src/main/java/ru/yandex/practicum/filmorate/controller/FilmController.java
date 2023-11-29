package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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
     * Поиск фильмов по строке поиска и заданным полям.
     *
     * @param query Строка запроса.
     * @param by тип поиска через запятую. Возможные значения: director, title
     * @return Список фильмов.
     */
    @GetMapping("/search")
    public List<Film> searchByQueryAndType(@RequestParam("query") String query,
            @RequestParam("by") String by) {
        return filmService.searchByQueryAndType(query, by);
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
     * Получение наиболее популярных фильмов по количеству лайков.
     */
    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }

    /**
     * Получение списка фильмов по режисеру, с сортировкой
     * @param directorId
     * @param sortBy
     * @return
     */
    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable Long directorId, @RequestParam String sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }
}
