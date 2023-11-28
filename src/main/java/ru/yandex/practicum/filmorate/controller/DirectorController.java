package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для {@link Director}
 */
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    /**
     * Получение списка режиссеров
     */
    @GetMapping
    public List<Director> getAll() {
        return directorService.getAll();
    }

    /**
     * Получение режисера по идентификатору
     *
     * @param directorId - идентификатор режиссера
     */
    @GetMapping("/{directorId}")
    public Director getById(@PathVariable Long directorId) {
        return directorService.getById(directorId);
    }

    /**
     * Создание режиссера
     */
    @PostMapping
    public Director create(@Valid @RequestBody Director director) {
        return directorService.create(director);
    }

    /**
     * Обновление режиссера
     */
    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        return directorService.update(director);
    }

    /**
     * Удаление режиссера по идентификатору
     * @param directorId - идентификатор режиссера
     */
    @DeleteMapping("/{directorId}")
    public void delete(@PathVariable Long directorId) {
        directorService.delete(directorId);
    }
}