package ru.yandex.practicum.filmorate.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

/**
 * Контроллер для {@link RatingMpa}.
 */
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingMpaController {

    private final RatingMpaService ratingMpaService;

    /**
     * Получение всех рейтингов MPA.
     */
    @GetMapping
    public List<RatingMpa> getAll() {
        return ratingMpaService.getAll();
    }

    /**
     * Получение рейтинга MPA по идентификатору.
     */
    @GetMapping("/{id}")
    public RatingMpa getById(@PathVariable Long id) {
        return ratingMpaService.getById(id);
    }
}
