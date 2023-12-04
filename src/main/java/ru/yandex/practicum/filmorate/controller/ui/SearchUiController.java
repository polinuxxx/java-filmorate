package ru.yandex.practicum.filmorate.controller.ui;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/search")
public class SearchUiController {


    private final FilmController filmController;

    @GetMapping
    @Operation(summary = "Получение отзывов по идентификатору фильма")
    public String get(
            @RequestParam String query,
            @RequestParam String by,
            Model model) {
        List<FilmResponse> filmResponses = filmController.searchByQueryAndType(query, by);
        model.addAttribute("films", filmResponses);
        model.addAttribute("query", query);
        return "/ui/searchResult";
    }
}
