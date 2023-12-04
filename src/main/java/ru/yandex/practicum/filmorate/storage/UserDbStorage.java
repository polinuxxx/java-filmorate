package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Authority;
import ru.yandex.practicum.filmorate.model.User;

/**
 * DAO для {@link User}.
 */
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String MAIN_SELECT = "select id, name, email, login, birthday from users ";

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        String sql = MAIN_SELECT + "where id = ?";

        List<User> users = jdbcTemplate.query(sql, UserDbStorage::toUser, id);

        return users.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        String sql = MAIN_SELECT + "order by id";

        return jdbcTemplate.query(sql, UserDbStorage::toUser);
    }

    @Override
    @Transactional
    public User create(User item) {
        item.setPassword("123");
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        item.setId(simpleJdbcInsert.executeAndReturnKey(item.toMap()).longValue());

        Authority authority = Authority.builder()
                .id(UUID.randomUUID())
                .authority("CLIENT")
                .user_id(item.getId())
                .build();

        SimpleJdbcInsert authorityJbdcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("authorities");

        authorityJbdcInsert.execute(authority.toMap());


        return getById(item.getId());
    }

    @Override
    @Transactional
    public User update(User item) {
        String sql = "update users set name = ?, login = ?, email = ?, birthday = ? where id = ?";

        jdbcTemplate.update(sql,
                item.getName(),
                item.getLogin(),
                item.getEmail(),
                item.getBirthday(),
                item.getId());

        return getById(item.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String sql = "delete from users where id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    @Transactional(readOnly = true)
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
