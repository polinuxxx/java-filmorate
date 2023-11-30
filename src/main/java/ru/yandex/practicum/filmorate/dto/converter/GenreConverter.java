package ru.yandex.practicum.filmorate.dto.converter;

import java.util.List;
import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.response.GenreResponse;
import ru.yandex.practicum.filmorate.model.Genre;

/**
 * Конвертер для {@link Genre}.
 */
@Mapper(componentModel = "spring")
public interface GenreConverter {


    GenreResponse convert(Genre genre);

    List<GenreResponse> convert(List<Genre> genres);

}
