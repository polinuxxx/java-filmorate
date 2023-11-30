package ru.yandex.practicum.filmorate.dto.converter;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.filmorate.dto.request.FilmCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.FilmResponse;
import ru.yandex.practicum.filmorate.model.Film;

/**
 * Конвертер для {@link Film}.
 */
@Mapper(componentModel = "spring")
public interface FilmConverter {

    @Mapping(source = "mpa", target = "mpa")
    @Mapping(source = "directors", target = "directors")
    @Mapping(source = "genres", target = "genres")
    FilmResponse convert(Film film);

    @Mapping(source = "mpa", target = "mpa")
    @Mapping(source = "directors", target = "directors")
    @Mapping(source = "genres", target = "genres")
    List<FilmResponse> convert(List<Film> films);


    @Mapping(source = "mpa", target = "mpa")
    @Mapping(source = "directors", target = "directors")
    @Mapping(source = "genres", target = "genres")
    Film convert(FilmCreateRequest request);

    @Mapping(source = "mpa", target = "mpa")
    @Mapping(source = "directors", target = "directors")
    @Mapping(source = "genres", target = "genres")
    Film convert(FilmUpdateRequest request);

}
