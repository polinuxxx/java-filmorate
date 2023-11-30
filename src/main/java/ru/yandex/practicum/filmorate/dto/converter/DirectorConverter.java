package ru.yandex.practicum.filmorate.dto.converter;

import java.util.List;
import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.request.DirectorCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.DirectorUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.DirectorResponse;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;

/**
 * Конвертер для {@link Genre}.
 */
@Mapper(componentModel = "spring")
public interface DirectorConverter {


    DirectorResponse convert(Director director);

    List<DirectorResponse> convert(List<Director> directors);

    Director convert(DirectorCreateRequest request);

    Director convert(DirectorUpdateRequest request);

}
