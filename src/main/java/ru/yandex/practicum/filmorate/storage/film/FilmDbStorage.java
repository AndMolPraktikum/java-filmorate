package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Qualifier
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> findAll() {
        final String sql = "SELECT f.film_id AS id,\n" +
                "       f.name AS name,\n" +
                "       f.description AS description,\n" +
                "       f.release_year AS release_year,\n" +
                "       f.duration AS duration,\n" +
                "       f.rate AS rate,\n" +
                "       f.mpa_id AS mpa_id,\n" +
                "       m.name AS mpa,\n" +
                "       SUM(l.grade) AS likes\n" +
                "FROM films f JOIN mpas m ON f.mpa_id = m.mpa_id\n" +
                "             LEFT JOIN likes l ON f.film_id = l.film_id\n" +
                "GROUP BY id;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film get(long key) {
        final String sql = "SELECT f.film_id AS id,\n" +
                "       f.name AS name,\n" +
                "       f.description AS description,\n" +
                "       f.release_year AS release_year,\n" +
                "       f.duration AS duration,\n" +
                "       f.rate AS rate,\n" +
                "       f.mpa_id AS mpa_id,\n" +
                "       m.name AS mpa,\n" +
                "       SUM(l.grade) AS likes\n" +
                "FROM films f JOIN mpas m ON f.mpa_id = m.mpa_id\n" +
                "             LEFT JOIN likes l ON f.film_id = l.film_id\n" +
                "WHERE f.film_id = ?\n" +
                "GROUP BY id;";

        final List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), key);
        if (films.size() != 1) {
            log.error("Фильм с таким ID не существует: {}", key);
            throw new FilmNotFoundException(String.format("Фильм с ID %d не существует", key));
        }
        return films.get(0);
    }

    @Override
    public Film create(Film film) {
        final String createFilmSql = "INSERT INTO films (name, description, release_year, rate, duration, mpa_id)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(createFilmSql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, film.getName());
            pst.setString(2, film.getDescription());
            pst.setDate(3, Date.valueOf(film.getReleaseDate()));
            pst.setInt(4, film.getRate());
            pst.setLong(5, film.getDuration());
            pst.setInt(6, film.getMpa().getId());
            return pst;
        }, keyHolder);

        long createdId = keyHolder.getKey().longValue();

        if (film.getGenres() != null) {
            final String createFilmGenreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(createFilmGenreSql, createdId, genre.getId());
            }
        }

        return get(createdId);
    }

    @Override
    public Film update(long key, Film film) {
        final String updateFilmSql = "UPDATE  films SET name = ?, description = ?, release_year = ?, duration = ?, \n" +
                "rate = ?, mpa_id = ? WHERE film_id = ?; DELETE FROM film_genres WHERE film_id = ?; ";

        jdbcTemplate.update(updateFilmSql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), key, key);

        if (film.getGenres() != null) {
            final String createFilmGenreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(createFilmGenreSql, key, genre.getId());
            }
        }
        return get(key);
    }

    @Override
    public void like(long filmId, long userId) {
        final String addLike = "INSERT INTO likes (film_id, user_id, grade) " +
                "VALUES (?, ?, 1);";

        jdbcTemplate.update(addLike, filmId, userId);
    }

    @Override
    public boolean likeCheck(long filmId, long userId) {
        final String likeCheck = "SELECT like_id FROM likes WHERE film_id = ? AND user_id = ?;";
        int count = 0;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(likeCheck, filmId, userId);
        while (filmRows.next()) {
            count++;
        }
        return count > 0;
    }

    @Override
    public void removeLike(long filmId, long userId) {
        final String removeLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";

        jdbcTemplate.update(removeLike, filmId, userId);
    }

    @Override
    public void remove(long key) {
        final String removeFilm = "DELETE FROM films WHERE film_id = ?;";

        jdbcTemplate.update(removeFilm, key);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_year").toLocalDate();
        long duration = rs.getLong("duration");
        int rate = rs.getInt("rate");
        long likes = rs.getLong("likes");
        Set<Genre> genres = getSetGenres(id);
        Mpa mpa = new Mpa(rs.getInt("mpa_id"), rs.getString("mpa"));

        return new Film(id, name, description, releaseDate, duration, rate, likes, genres, mpa);
    }

    private Set<Genre> getSetGenres(long id) {
        final String filmGenres = "SELECT g.genre_id, g.name FROM film_genres fg JOIN genres g " +
                "ON fg.genre_id = g.genre_id WHERE fg.film_id = ? GROUP BY g.genre_id;";

        return new HashSet<Genre>(jdbcTemplate.query(filmGenres, (rs, rowNum) -> makeGenre(rs), id));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }
}
