package ru.yandex.practicum.filmorate.storage;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Genre;

/**
 * DAO для жанров фильмов.
 */
@Component
@RequiredArgsConstructor
public class FilmGenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void addGenreToFilm(Long filmId, Long genreId) {
        jdbcTemplate.update("insert into film_genres (film_id, genre_id) values (?, ?)",
                filmId, genreId);
    }

    @Transactional
    public void addGenresToFilm(Long filmId, Set<Genre> genres) {
        genres.forEach(genre -> addGenreToFilm(filmId, genre.getId()));
    }

    @Transactional
    public void deleteGenresFromFilm(Long filmId) {
        jdbcTemplate.update("delete from film_genres where film_id = ?", filmId);
    }

    @Transactional(readOnly = true)
    public boolean exists(Long filmId, Long genreId) {
        String sql = "select exists(select film_id from film_genres where film_id = ? and genre_id = ?)";

        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, filmId, genreId);

        return exists != null && exists;
    }
}
