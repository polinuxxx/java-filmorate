package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.converter.FilmConverter;
import ru.yandex.practicum.filmorate.dto.request.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

/**
 * Контроллер для {@link Film}.
 */
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Tag(name = "Films", description = "Управление фильмами")
public class FilmController {

    private final FilmService filmService;
    private final FilmConverter filmConverter;

    /**
     * Получение всех фильмов.
     */
    @GetMapping
    @Operation(summary = "Получение всех фильмов")
    public List<FilmResponse> getAll() {
        return filmConverter.convert(filmService.getAll());
    }

    /**
     * Получение фильма по идентификатору.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение фильма по id")
    public FilmResponse getById(@PathVariable Long id) {
        return filmConverter.convert(filmService.getById(id));
    }

    /**
     * Поиск фильмов по строке поиска и заданным полям.
     *
     * @param query Строка запроса.
     * @param by тип поиска через запятую. Возможные значения: director, title
     * @return Список фильмов.
     */
    @GetMapping("/search")
    @Operation(summary = "Поиск фильма по строке запроса, по полям наименование, режиссер или и там и там")
    public List<FilmResponse> searchByQueryAndType(@RequestParam("query") String query,
            @RequestParam("by") String by) {
        return filmConverter.convert(filmService.searchByQueryAndType(query, by));
    }

    /**
     * Создание фильма.
     */
    @PostMapping
    @Operation(summary = "Создание фильма")
    public FilmResponse create(@RequestBody @Valid FilmCreateRequest film) {
        return filmConverter.convert(filmService.create(filmConverter.convert(film)));
    }

    /**
     * Редактирование фильма.
     */
    @PutMapping
    @Operation(summary = "Обновление фильма")
    public FilmResponse update(@RequestBody @Valid FilmUpdateRequest film) {
        return filmConverter.convert(filmService.update(filmConverter.convert(film)));
    }

    /**
     * Удаление фильма.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление фильма")
    public void delete(@PathVariable Long id) {
        filmService.delete(id);
    }

    /**
     * Добавление фильму лайка.
     */
    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "Добавление лайка от пользователя к фильму")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    /**
     * Удаление у фильма лайка.
     */
    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "Удаление лайка пользователя у фильма")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    /**
     * Получение наиболее популярных фильмов по количеству лайков
     * Опционально: в конкретном жанре или выпущенных в определенном году.
     */
    @GetMapping("/popular")
    @Operation(summary = "Получение популярных фильмов ( опционально - по году и/или жанру) , по умолчанию 10 шт.")
    public List<FilmResponse> getPopular(@RequestParam(defaultValue = "10") int count,
                                         @RequestParam(required = false) Integer genreId,
                                         @RequestParam(required = false) Integer year) {
        return filmConverter.convert(filmService.getPopular(count,genreId,year));
    }

    /**
     * Получение списка фильмов по режиссеру, с сортировкой
     * @param directorId идентификатор режиссера
     * @param sortBy тип сортировки
     * @return список фильмов
     */
    @GetMapping("/director/{directorId}")
    @Operation(summary = "Получение фильмов по id режиссера")
    public List<FilmResponse> getFilmsByDirector(@PathVariable Long directorId, @RequestParam String sortBy) {
        return filmConverter.convert(filmService.getFilmsByDirector(directorId, sortBy));
    }
}
