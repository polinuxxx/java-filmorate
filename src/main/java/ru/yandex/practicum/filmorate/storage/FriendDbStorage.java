package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

/**
 * DAO для друзей пользователей.
 */
@Component
@RequiredArgsConstructor
public class FriendDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public List<User> getByUserId(Long userId) {

        String sql = "select id, email, login, name, birthday from users left join friends on id = friend_id " +
                "where user_id = ? order by id";

        return jdbcTemplate.query(sql, FriendDbStorage::toUser, userId);
    }

    public void addFriendToUser(Long userId, Long friendId) {

        if (userId.equals(friendId)) {
            throw new ValidationException(String.format("Невозможно добавить в друзья самого себя для пользователя с" +
                    " id = %d", userId));
        }

        if (exists(userId, friendId)) {
            throw new EntityAlreadyExistsException(String.format("У пользователя с id = %d уже есть друг " +
                    "с id = %d", userId, friendId));
        }
        jdbcTemplate.update("insert into friends (user_id, friend_id) values (?, ?)",
                userId, friendId);
    }

    public void deleteFriendFromUser(Long userId, Long friendId) {

        if (!exists(userId, friendId)) {
            throw new EntityNotFoundException(String.format("У пользователя с id = %d отсутствует друг" +
                    " с id = %d", userId, friendId));
        }

        jdbcTemplate.update("delete from friends where user_id = ? and friend_id = ?", userId, friendId);
    }

    public boolean exists(Long userId, Long friendId) {
        String sql = "select case when count(user_id) > 0 then true else false end " +
                "from friends where user_id = ? and friend_id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, userId, friendId);

        return exists != null && exists;
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        String sql = "select id, name, email, login, birthday from users left join friends on id = friend_id " +
                "where user_id = ? intersect " +
                "select id, name, email, login, birthday from users left join friends on id = friend_id " +
                "where user_id = ? order by id";

        return jdbcTemplate.query(sql, FriendDbStorage::toUser, userId, friendId);
    }

    static User toUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
