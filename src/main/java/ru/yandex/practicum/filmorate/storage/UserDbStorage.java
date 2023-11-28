package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        item.setId(simpleJdbcInsert.executeAndReturnKey(item.toMap()).longValue());

        return getById(item.getId());
    }

    /**
     * Получение списка id рекомендуемых фильмов по id пользователя.
     *
     * @param id пользователя
     * @return список фильмов
     */
    @Override
    @Transactional(readOnly = true)
    public List<Long> getRecommendationsFilmIDs(Long id) {

        /*
        -- Собираем таблицу пользователей с похожими предпочтениями с частотой совпадений
        -- USER1_ID - ключевой пользователь
        -- USER2_ID - похожий пользователь
        -- CNT - частота совпадений предпочтений

        -- Склеиваем похожих пользователей с фильмами
        -- Исключаем фильмы с отметками ключевого пользователя
        */

        String sql = "SELECT likesThree.FILM_ID " +
                "FROM (SELECT likesOne.USER_ID AS USER1_ID, likesTwo.USER_ID AS USER2_ID, COUNT(*) AS CNT " +
                "      FROM LIKES likesOne " +
                "               INNER JOIN LIKES likesTwo " +
                "                          ON likesOne.FILM_ID = likesTwo.FILM_ID " +
                "                              AND likesOne.USER_ID <> likesTwo.USER_ID AND likesOne.USER_ID = ? " +
                "      GROUP BY likesTwo.USER_ID) AS near " +
                "         INNER JOIN LIKES likesThree " +
                "                    ON near.USER2_ID = likesThree.USER_ID " +
                "WHERE likesThree.FILM_ID NOT EXIST (SELECT FILM_ID FROM LIKES WHERE USER_ID = near.USER1_ID) " +
                "ORDER BY near.CNT DESC ";

        return jdbcTemplate.queryForList(sql, Long.class, id);

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
