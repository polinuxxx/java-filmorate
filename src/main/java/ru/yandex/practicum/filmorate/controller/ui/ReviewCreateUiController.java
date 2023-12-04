package ru.yandex.practicum.filmorate.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.dto.request.ReviewCreateRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/review_create")
public class ReviewCreateUiController {

    private final ReviewController reviewController;
    private final FilmController filmController;
    /**
     * Отобразить страницу для создания нового графика.
     *
     * @param model модель
     * @return шаблон для создания нового графика
     */
    @GetMapping()
    public String showCreateReviewForm(Model model,
            @RequestParam(required = false) Long filmId) {
        model.addAttribute("review", new ReviewCreateRequest() );
        model.addAttribute("film", filmController.getById(filmId));
        return "/ui/review_create";
    }
}
