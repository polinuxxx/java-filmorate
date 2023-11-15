package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

/**
 * DAO для {@link User}.
 */
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getById(Long id) {
        String sql = "select id, name, email, login, birthday from users where id = ?";

        List<User> users = jdbcTemplate.query(sql, UserDbStorage::toUser, id);

        return users.get(0);
    }

    @Override
    public List<User> getAll() {
        String sql = "select id, name, email, login, birthday from users order by id";

        return jdbcTemplate.query(sql, UserDbStorage::toUser);
    }

    @Override
    public User create(User item) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        item.setId(simpleJdbcInsert.executeAndReturnKey(item.toMap()).longValue());

        return item;
    }

    @Override
    public User update(User item) {
        String sql = "update users set name = ?, login = ?, email = ?, birthday = ? where id = ?";

        jdbcTemplate.update(sql,
                item.getName(),
                item.getLogin(),
                item.getEmail(),
                item.getBirthday(),
                item.getId());

        return item;
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from users where id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean exists(Long id) {
        String sql = "select case when count(id) > 0 then true else false end from users where id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
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
