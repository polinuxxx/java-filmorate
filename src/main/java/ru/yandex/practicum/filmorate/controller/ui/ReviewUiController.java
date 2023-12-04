package ru.yandex.practicum.filmorate.controller.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;
import ru.yandex.practicum.filmorate.dto.response.ReviewResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/reviews")
public class ReviewUiController {

    private final ReviewController reviewController;
    private final FilmController filmController;

    @GetMapping
    public String get(
            @RequestParam(required = false) Long filmId,
            @RequestParam(defaultValue = "10") Integer count,
            Model model) {
        FilmResponse filmResponse = filmController.getById(filmId);
        List<ReviewResponse> reviewResponses =  reviewController.get(filmId, count );
        model.addAttribute("film", filmResponse);
        model.addAttribute("reviews", reviewResponses);
        return "/ui/reviews";
    }
}
