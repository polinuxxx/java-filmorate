package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.converter.DirectorConverter;
import ru.yandex.practicum.filmorate.dto.request.DirectorCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.DirectorUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.DirectorResponse;
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
@Tag(name = "Directors", description = "Управление режиссерами")
public class DirectorController {

    private final DirectorService directorService;
    private final DirectorConverter directorConverter;

    /**
     * Получение списка режиссеров
     */
    @GetMapping
    @Operation(summary = "Получение всех режиссеров")
    public List<DirectorResponse> getAll() {
        return directorConverter.convert(directorService.getAll());
    }

    /**
     * Получение режиссера по идентификатору
     *
     * @param directorId - идентификатор режиссера
     */
    @GetMapping("/{directorId}")
    @Operation(summary = "Получение режиссера по id")
    public DirectorResponse getById(@PathVariable Long directorId) {
        return directorConverter.convert(directorService.getById(directorId));
    }

    /**
     * Создание режиссера
     */
    @PostMapping
    @Operation(summary = "Создание режиссера")
    public DirectorResponse create(@Valid @RequestBody DirectorCreateRequest director) {
        return directorConverter.convert(directorService.create(directorConverter.convert(director)));
    }

    /**
     * Обновление режиссера
     */
    @PutMapping
    @Operation(summary = "Обновление режиссера")
    public DirectorResponse update(@Valid @RequestBody DirectorUpdateRequest director) {
        return directorConverter.convert(directorService.update(directorConverter.convert(director)));
    }

    /**
     * Удаление режиссера по идентификатору
     *
     * @param directorId - идентификатор режиссера
     */
    @DeleteMapping("/{directorId}")
    @Operation(summary = "Удаление режиссера")
    public void delete(@PathVariable Long directorId) {
        directorService.delete(directorId);
    }
}