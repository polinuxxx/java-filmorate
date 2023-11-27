package ru.yandex.practicum.filmorate.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

    private static final String MAIN_SELECT = "select films.id, films.name, films.description, films.duration_min, "
            + "films.release_date, films.rating_mpa_id as mpa_id, mpa.name as mpa_name, mpa.description "
            + "as mpa_description, genres.id as genre_id, genres.name as genre_name from films "
            + "left join rating_mpa mpa on films.rating_mpa_id = mpa.id "
            + "left join film_genres on films.id = film_genres.film_id "
            + "left join genres on film_genres.genre_id = genres.id ";

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
        String sql = MAIN_SELECT + "order by films.id";

        return getCompleteFilmFromQuery(sql);
    }

    @Override
    public List<Film> search(String query, HashSet<String> by) {
        String sql = MAIN_SELECT + "where 1=1 ";
        ArrayList<String> listParams = new ArrayList<>();
        Iterator<String> it = by.iterator();

        while (it.hasNext()) {
            switch (it.next()) {
                case "director":
                    sql = sql + " AND LOWER(films.director) like ?";
                    listParams.add("%" + query.toLowerCase() + "%");
                    throw new ParamNotExistException("Директора пока не реализованы");
                    //break;
                case "title":
                    sql = sql + " AND LOWER(films.name) like ?";
                    listParams.add("%" + query.toLowerCase() + "%");
                    break;
                default:
                    throw new ParamNotExistException("Такой команды для поиска пока нет.");
            }
        }

        return getCompleteFilmFromQuery(sql, listParams.toArray());
    }

    @Override
    @Transactional
    public Film create(Film item) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("id");

        Long filmId = simpleJdbcInsert.executeAndReturnKey(item.toMap()).longValue();

        return getById(filmId);
    }

    @Override
    @Transactional
    public Film update(Film item) {
        String sql = "update films set name = ?, description = ?, release_date = ?, duration_min = ?, "
                + "rating_mpa_id = ? where id = ?";

        jdbcTemplate.update(sql, item.getName(), item.getDescription(), item.getReleaseDate(), item.getDuration(),
                item.getMpa().getId(), item.getId());

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
    public List<Film> getPopular(int count) {
        String sql = MAIN_SELECT + "left join likes on films.id = likes.film_id "
                + "group by films.id, genre_id order by count(likes.user_id) desc limit ?";

        return getCompleteFilmFromQuery(sql, count);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long id) {
        String sql = "select case when count(id) > 0 then true else false end from films where id = ?";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);

        return exists != null && exists;
    }

    private Genre constructGenreFromQueryResult(ResultSet rs) throws SQLException {
        return Genre.builder().id(rs.getLong("genre_id")).name(rs.getString("genre_name")).build();
    }

    private RatingMpa constructRatingMpaFromQueryResult(ResultSet rs) throws SQLException {
        return RatingMpa.builder().id(rs.getLong("mpa_id")).name(rs.getString("mpa_name"))
                .description(rs.getString("mpa_description")).build();
    }

    private Film constructFilmFromQueryResult(ResultSet rs) throws SQLException {
        return Film.builder().id(rs.getLong("id")).name(rs.getString("name")).description(rs.getString("description"))
                .duration(rs.getInt("duration_min")).releaseDate(rs.getDate("release_date").toLocalDate())
                .genres(new HashSet<>()).build();
    }

    private List<Film> getCompleteFilmFromQuery(String sql, Object... params) {
        return jdbcTemplate.query(sql, rs -> {
            Map<Long, Film> map = new HashMap<>();
            while (rs.next()) {
                if (!map.containsKey(rs.getLong("id"))) {
                    Film film = constructFilmFromQueryResult(rs);

                    if (rs.getLong("mpa_id") != 0) {
                        film.setMpa(constructRatingMpaFromQueryResult(rs));
                    }

                    if (rs.getLong("genre_id") != 0) {
                        film.getGenres().add(constructGenreFromQueryResult(rs));
                    }

                    map.put(film.getId(), film);
                } else {
                    Film film = map.get(rs.getLong("id"));
                    if (rs.getLong("genre_id") != 0) {
                        film.getGenres().add(constructGenreFromQueryResult(rs));
                    }
                }
            }
            return map.isEmpty() ? new ArrayList<>() : new ArrayList<>(map.values());
        }, params);
    }
}
