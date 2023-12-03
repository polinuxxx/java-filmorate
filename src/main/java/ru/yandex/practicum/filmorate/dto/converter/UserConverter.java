package ru.yandex.practicum.filmorate.dto.converter;

import java.util.List;
import org.mapstruct.Mapper;
import ru.yandex.practicum.filmorate.dto.request.UserCreateRequest;
import ru.yandex.practicum.filmorate.dto.request.UserUpdateRequest;
import ru.yandex.practicum.filmorate.dto.response.UserResponse;
import ru.yandex.practicum.filmorate.model.User;

/**
 * Конвертер для создания {@link User}
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    User convert(UserCreateRequest request);

    User convert(UserUpdateRequest request);

    UserResponse convert(User user);

    List<UserResponse> convert(List<User> users);
}
