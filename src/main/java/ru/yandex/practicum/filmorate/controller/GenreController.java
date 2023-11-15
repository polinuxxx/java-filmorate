package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

/**
 * Контроллер для {@link Genre}.
 */
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    /**
     * Получение всех жанров.
     */
    @GetMapping
    public List<Genre> getAll() {
        return genreService.getAll();
    }

    /**
     * Получение жанра по идентификатору.
     */
    @GetMapping("/{id}")
    public Genre getById(@PathVariable Long id) {
        return genreService.getById(id);
    }
}
