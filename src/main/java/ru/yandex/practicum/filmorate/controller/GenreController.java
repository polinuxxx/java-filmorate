package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.converter.GenreConverter;
import ru.yandex.practicum.filmorate.dto.response.GenreResponse;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

/**
 * Контроллер для {@link Genre}.
 */
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Tag(name = "Genres", description = "Управление жанрами")
public class GenreController {

    private final GenreService genreService;
    private final GenreConverter genreConverter;

    /**
     * Получение всех жанров.
     */
    @GetMapping
    @Operation(summary = "Получение всех жанров")
    public List<GenreResponse> getAll() {
        return genreConverter.convert(genreService.getAll());
    }

    /**
     * Получение жанра по идентификатору.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение жанра по id")
    public GenreResponse getById(@PathVariable Long id) {
        return genreConverter.convert(genreService.getById(id));
    }
}
