package ru.yandex.practicum.filmorate.controller.ui;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;
import ru.yandex.practicum.filmorate.dto.response.UserResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/fragments/layouts")
public class LayoutsUiController {

    private final UserController userController;
    @GetMapping
    public String getAll(Model model, Principal principal) {
        model.addAttribute("login", principal.getName());
        return "/ui/fragments/layouts";
    }
}
