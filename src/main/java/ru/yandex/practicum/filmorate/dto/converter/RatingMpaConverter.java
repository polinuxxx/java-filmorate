package ru.yandex.practicum.filmorate.dto.converter;

import java.util.List;
import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.response.RatingMpaResponse;
import ru.yandex.practicum.filmorate.model.RatingMpa;

/**
 * Конвертер для {@link RatingMpa}.
 */
@Mapper(componentModel = "spring")
public interface RatingMpaConverter {


    RatingMpaResponse convert(RatingMpa ratingMpa);

    List<RatingMpaResponse> convert(List<RatingMpa> ratingsMpa);

}
