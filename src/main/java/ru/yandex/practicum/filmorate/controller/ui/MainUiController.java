package ru.yandex.practicum.filmorate.controller.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainUiController {

    private final FilmController filmController;

    @GetMapping
    public String getAllFilms(Model model) {
        List<FilmResponse> filmControllerAll = filmController.getAll();
        model.addAttribute("films", filmControllerAll);
        return "main";
    }


}

