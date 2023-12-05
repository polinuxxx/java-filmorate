package ru.yandex.practicum.filmorate.controller.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dto.request.ReviewCreateRequest;
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

    @GetMapping("/create")
    public String showCreateReviewForm(Model model,
            @RequestParam(required = false) Long filmId, Authentication authentication) {
        model.addAttribute("review", new ReviewCreateRequest() );
        model.addAttribute("film", filmController.getById(filmId));
        model.addAttribute("user", userController.getByLogin(authentication.getName()));
        return "/ui/review_create";
    }

    @PostMapping
    public String createReview(@ModelAttribute("review") ReviewCreateRequest reviewCreateRequest) {
        reviewController.create(reviewCreateRequest);
        return "redirect:/ui/reviews?filmId=" + reviewCreateRequest.getFilmId();
    }
}
