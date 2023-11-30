package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.converter.RatingMpaConverter;
import ru.yandex.practicum.filmorate.dto.response.RatingMpaResponse;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

/**
 * Контроллер для {@link RatingMpa}.
 */
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Tag(name = "Mpa", description = "Управление рейтингами MPA")
public class RatingMpaController {

    private final RatingMpaService ratingMpaService;
    private final RatingMpaConverter ratingMpaConverter;

    /**
     * Получение всех рейтингов MPA.
     */
    @GetMapping
    @Operation(summary = "Получение всех рейтингов")
    public List<RatingMpaResponse> getAll() {
        return ratingMpaConverter.convert(ratingMpaService.getAll());
    }

    /**
     * Получение рейтинга MPA по идентификатору.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение рейтинга по id")
    public RatingMpaResponse getById(@PathVariable Long id) {
        return ratingMpaConverter.convert(ratingMpaService.getById(id));
    }
}
