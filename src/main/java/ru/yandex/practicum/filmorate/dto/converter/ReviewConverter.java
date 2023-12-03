package ru.yandex.practicum.filmorate.dto.converter;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.filmorate.dto.request.ReviewCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.ReviewUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.ReviewResponse;
import ru.yandex.practicum.filmorate.model.Review;

/**
 * Конвертер для {@link Review}.
 */
@Mapper(componentModel = "spring")
public interface ReviewConverter {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "film.id", target = "filmId")
    ReviewResponse convert(Review review);

    List<ReviewResponse> convert(List<Review> reviews);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "film.id", source = "filmId")
    Review convert(ReviewCreateRequest request);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "film.id", source = "filmId")
    Review convert(ReviewUpdateRequest request);
}
