package ru.yandex.practicum.filmorate.controller.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;
import ru.yandex.practicum.filmorate.dto.response.ReviewResponse;
import ru.yandex.practicum.filmorate.dto.response.UserResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ui/reviews")
public class ReviewUiController {

    private final ReviewController reviewController;
    private final FilmController filmController;
    private final UserController userController;

    @GetMapping
    public String get(
            @RequestParam(required = false) Long filmId,
            @RequestParam(defaultValue = "10") Integer count,
            Model model) {
        FilmResponse filmResponse = filmController.getById(filmId);
        List<ReviewResponse> reviewResponses =  reviewController.get(filmId, count );
        Map<Long, UserResponse> userResponseMap = new HashMap<>();
        reviewResponses.stream().forEach(it -> userResponseMap.put(it.getUserId(), userController.getById(it.getUserId())));
        model.addAttribute("film", filmResponse);
        model.addAttribute("users", userResponseMap);
        model.addAttribute("reviews", reviewResponses);
        return "/ui/reviews";
    }
}
