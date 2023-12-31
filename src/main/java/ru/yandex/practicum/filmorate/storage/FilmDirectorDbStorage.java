package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Set;

/**
 * DAO для режисеров / фильмов.
 */
@Component
@RequiredArgsConstructor
public class FilmDirectorDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void addDirectorToFilm(Long filmId, Long directorId) {
        jdbcTemplate.update("insert into film_directors (film_id, director_id) values (?, ?)",
                filmId, directorId);
    }

    @Transactional
    public void addDirectorToFilm(Long filmId, Set<Director> directors) {
        directors.forEach(director -> addDirectorToFilm(filmId, director.getId()));
    }

    @Transactional
    public void deleteDirectorsFromFilm(Long filmId) {
        jdbcTemplate.update("delete from film_directors where film_id = ?", filmId);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long filmId, Long genreId) {
        String sql = "select exists(select from film_directors where film_id = ? and director_id = ?)";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, filmId, genreId);

        return exists != null && exists;
    }
}
