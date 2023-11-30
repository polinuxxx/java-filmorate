package ru.yandex.practicum.filmorate.dto.converter;

import java.util.List;
import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.request.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Конвертер для {@link Film}.
 */
@Mapper(componentModel = "spring")
public interface FilmConverter {

    FilmResponse convert(Film film);

    List<FilmResponse> convert(List<Film> films);

    Film convert(FilmCreateRequest request);

    Film convert(FilmUpdateRequest request);

}
