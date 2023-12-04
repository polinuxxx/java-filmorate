package ru.yandex.practicum.filmorate.controller.ui;

import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/")
public class MainUiController {

    private final FilmController filmController;

    @GetMapping
    public String getAllFilms(@RequestParam(required = false) Long filmId,Model model) {
        List<FilmResponse> filmControllerAll = filmController.getAll();
        model.addAttribute("films", filmControllerAll);
        return "main";
    }


}

