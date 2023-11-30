package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.exception.ParamNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

/**
 * DAO для {@link Film}.
 */
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String MAIN_SELECT = "select films.id, films.name, films.description, films.duration_min, " +
            "films.release_date, films.rating_mpa_id as mpa_id, mpa.name as mpa_name, mpa.description " +
            "as mpa_description, genres.id as genre_id, genres.name as genre_name, " +
            "directors.id as director_id, directors.name as director_name " +
            "from films " +
            "left join rating_mpa mpa on films.rating_mpa_id = mpa.id " +
            "left join film_genres on films.id = film_genres.film_id " +
            "left join film_directors on films.id = film_directors.film_id " +
            "left join directors on film_directors.director_id = directors.id " +
            "left join genres on film_genres.genre_id = genres.id ";

    private final JdbcTemplate jdbcTemplate;


    @Override
    @Transactional(readOnly = true)
    public Film getById(Long id) {

        String sql = MAIN_SELECT + "where films.id = ? order by genre_id";

        List<Film> films = getCompleteFilmFromQuery(sql, id);

        return films.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Film> getAll() {
        String sql = MAIN_SELECT + "order by films.id, genre_id";

        return getCompleteFilmFromQuery(sql);
    }

    /**
     * Метод для поиска фильмов по строке поиска в любом регистре и переданным полям для поиска.
     *
     * @param query строка для поиска
     * @param by поля для поиска через запятую, варианты: director, title
     * @return список фильмов.
     */
    @Override
    public List<Film> getFilmsByQueryAndType(String query, String by) {

        Set<String> bySet = new HashSet<>(Arrays.asList(by.split(",")));
        String sql = MAIN_SELECT + "left join likes on films.id = likes.film_id ";
        ArrayList<String> listParams = new ArrayList<>();
        String byToQuery = "";
        Iterator<String> it = bySet.iterator();

        while (it.hasNext()) {
            switch (it.next()) {
                case "director":
                    byToQuery = byToQuery + "LOWER(directors.name) like ? ";
                    listParams.add("%" + query.toLowerCase() + "%");
                    break;
                case "title":
                    byToQuery = byToQuery + "LOWER(films.name) like ? ";
                    listParams.add("%" + query.toLowerCase() + "%");
                    break;
                default:
                    throw new ParamNotExistException("Такой команды для поиска пока нет.");
            }

            if (it.hasNext()) {
                byToQuery = byToQuery + " OR ";
            }
        }

        if (!byToQuery.isEmpty()) {
            sql = sql + "where " + byToQuery + " group by films.id, genre_id order by count(likes.user_id) desc";
        }

        return getCompleteFilmFromQuery(sql, listParams.toArray());
    }

    /**
     * Получение списка рекомендуемых фильмов по айди пользователя.
     *
     * @param userId id пользователя
     * @return List фильмов.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Film> getRecommendationFilms(Long userId) {

        /*
        -- Собираем таблицу пользователей с похожими предпочтениями с частотой совпадений
        -- USER1_ID - ключевой пользователь
        -- USER2_ID - похожий пользователь
        -- CNT - частота совпадений предпочтений

        -- Склеиваем похожих пользователей с фильмами
        -- Исключаем фильмы с отметками ключевого пользователя
        */

        String recommendedFilmIdsQuery = "SELECT likesThree.FILM_ID " +
                "FROM (SELECT likesOne.USER_ID AS USER1_ID, likesTwo.USER_ID AS USER2_ID, COUNT(*) AS CNT " +
                "      FROM LIKES likesOne " +
                "               INNER JOIN LIKES likesTwo " +
                "                          ON likesOne.FILM_ID = likesTwo.FILM_ID " +
                "                              AND likesOne.USER_ID <> likesTwo.USER_ID AND likesOne.USER_ID = ? " +
                "      GROUP BY likesTwo.USER_ID) AS near " +
                "         INNER JOIN LIKES likesThree " +
                "                    ON near.USER2_ID = likesThree.USER_ID " +
                "WHERE likesThree.FILM_ID NOT IN (SELECT FILM_ID FROM LIKES WHERE USER_ID = near.USER1_ID) " +
                "ORDER BY near.CNT DESC ";

        String sql = MAIN_SELECT + ", (" + recommendedFilmIdsQuery + ") as top where top.FILM_ID = films.ID order by genre_id";

        return getCompleteFilmFromQuery(sql, userId);
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String sql = "SELECT * FROM films " +
                "JOIN (SELECT film_id FROM likes WHERE user_id = ?) user_likes_1 ON films.id = user_likes_1.film_id " +
                "JOIN (SELECT film_id FROM likes WHERE user_id = ?) user_likes_2 ON films.id = user_likes_2.film_id " +
                "JOIN (SELECT film_id, COUNT(user_id) AS like_count FROM likes GROUP BY film_id) like_counts " +
                "ON films.id = like_counts.film_id " +
                "ORDER BY like_counts.like_count DESC";
        return getCompleteFilmFromQuery(sql, userId, friendId);
    }

    @Override
    @Transactional
    public Film create(Film item) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Long filmId = simpleJdbcInsert.executeAndReturnKey(item.toMap()).longValue();

        return getById(filmId);
    }

    @Override
    @Transactional
    public Film update(Film item) {
        String sql = "update films set name = ?, description = ?, release_date = ?, duration_min = ?, " +
                "rating_mpa_id = ? where id = ?";

        jdbcTemplate.update(sql,
                item.getName(),
                item.getDescription(),
                item.getReleaseDate(),
                item.getDuration(),
                item.getMpa().getId(),
                item.getId());

        return getById(item.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String sql = "delete from films where id = ?";

        jdbcTemplate.update(sql, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Film> getPopular(int count, Integer genreId, Integer year) {
        String sql = MAIN_SELECT + "left join likes on films.id = likes.film_id "
                + ", (select ID, count(likesOne.USER_ID) as cnt "
                + "      from FILMS "
                + "               left join likes likesOne on likesOne.FILM_ID = FILMS.ID "
                + "      group by id "
                + "      order by cnt desc "
                + "      limit ?) top "
                + "where 1 = 1 " +
                (!Objects.isNull(genreId) ? "and films.ID IN (SELECT FILM_ID FROM FILM_GENRES WHERE GENRE_ID = ?) " : "") +
                (!Objects.isNull(year) ? "and YEAR(FILMS.RELEASE_DATE) = ? " : "") +
                "AND films.ID = top.id " +
                "group by films.id, genre_id " +
                "order by top.cnt desc, genre_id";

        List<Object> params = new ArrayList<>();
        params.add(count);
        if (!Objects.isNull(genreId)) {
            params.add(genreId);
        }
        if (!Objects.isNull(year)) {
            params.add(year);
        }

        Object[] paramsArray = params.toArray(new Object[0]);
        return getCompleteFilmFromQuery(sql, paramsArray);
    }

    @Override
    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        String sql;

        if (sortBy.equals("likes")) {
            sql = MAIN_SELECT + "left join likes on films.id = likes.film_id " +
                    "where directors.id = ? " +
                    "group by films.id, genre_id order by count(likes.user_id) desc, genre_id";
        } else if (sortBy.equals("year")) {
            sql = MAIN_SELECT + "left join likes on films.id = likes.film_id " +
                    "where directors.id = ? " +
                    "group by films.id, genre_id order by release_date, genre_id";
        } else {
            throw new IllegalArgumentException("Неподдерживаемый параметр упорядочивания фильмов.");
        }

        return getCompleteFilmFromQuery(sql, directorId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        String sql = "select case when count(id) > 0 then true else false end from films where id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }

    private Genre constructGenreFromQueryResult(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

    private Director constructDirectorFromQueryResult(ResultSet rs) throws SQLException {
        return Director.builder()
                .id(rs.getLong("director_id"))
                .name(rs.getString("director_name"))
                .build();
    }

    private RatingMpa constructRatingMpaFromQueryResult(ResultSet rs) throws SQLException {
        return RatingMpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .description(rs.getString("mpa_description"))
                .build();
    }

    private Film constructFilmFromQueryResult(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration_min"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .build();
    }

    private List<Film> getCompleteFilmFromQuery(String sql, Object... params) {
        return jdbcTemplate.query(sql,
                rs -> {
                    Map<Long, Film> map = new LinkedHashMap<>();
                    while (rs.next()) {
                        if (!map.containsKey(rs.getLong("id"))) {
                            Film film = constructFilmFromQueryResult(rs);

                            if (rs.getLong("mpa_id") != 0) {
                                film.setMpa(constructRatingMpaFromQueryResult(rs));
                            }

                            if (rs.getLong("genre_id") != 0) {
                                film.getGenres().add(constructGenreFromQueryResult(rs));
                            }

                            if (rs.getLong("director_id") != 0) {
                                film.getDirectors().add(constructDirectorFromQueryResult(rs));
                            }

                            map.put(film.getId(), film);
                        } else {
                            Film film = map.get(rs.getLong("id"));
                            if (rs.getLong("genre_id") != 0) {
                                film.getGenres().add(constructGenreFromQueryResult(rs));
                            }

                            if (rs.getLong("director_id") != 0) {
                                film.getDirectors().add(constructDirectorFromQueryResult(rs));
                            }
                        }
                    }
                    return map.isEmpty() ? new ArrayList<>() : new ArrayList<>(map.values());
                }, params
        );
    }
}
