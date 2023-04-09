package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.Status;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        String sql = "SELECT t1.id,\n" +
                "       name, \n" +
                "       description,\n" +
                "       release_year,\n" +
                "       duration,\n" +
                "       mpas_id,\n" +
                "       mpa,\n" +
                "       likes,\n" +
                "       genres \n" +
                "FROM (\n" +
                "     SELECT f.id AS id,\n" +
                "            f.name AS name,\n" +
                "            f.description AS description,\n" +
                "            f.release_year AS release_year,\n" +
                "            f.duration AS duration,\n" +
                "            f.mpas_id AS mpas_id,\n" +
                "            m.name AS mpa,\n" +
                "            SUM(l.grade) AS likes\n" +
                "     FROM films f JOIN mpas m ON f.mpas_id = m.id\n" +
                "             LEFT JOIN likes l ON f.id = l.film_id\n" +
                "     GROUP BY id\n" +
                ") AS t1,\n" +
                "    (SELECT f.id AS id,\n" +
                "             array_agg(g.id) AS genres\n" +
                "     FROM films f LEFT JOIN film_genres fg ON f.id = fg.film_id\n" +
                "                  LEFT JOIN genres g ON fg.genre_id = g.id\n" +
                "     GROUP BY id\n" +
                ") AS t2\n" +
                "WHERE  t1.id = t2.id;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Optional<Film> get(long key) {
        String sql = "SELECT t1.id,\n" +
                "       name, \n" +
                "       description,\n" +
                "       release_year,\n" +
                "       duration,\n" +
                "       mpas_id,\n" +
                "       mpa,\n" +
                "       likes,\n" +
                "       genres \n" +
                "FROM (\n" +
                "     SELECT f.id AS id,\n" +
                "            f.name AS name,\n" +
                "            f.description AS description,\n" +
                "            f.release_year AS release_year,\n" +
                "            f.duration AS duration,\n" +
                "            f.mpas_id AS mpas_id,\n" +
                "            m.name AS mpa,\n" +
                "            SUM(l.grade) AS likes\n" +
                "     FROM films f JOIN mpas m ON f.mpas_id = m.id\n" +
                "             LEFT JOIN likes l ON f.id = l.film_id\n" +
                "     GROUP BY id\n" +
                ") AS t1,\n" +
                "    (SELECT f.id AS id,\n" +
                "             array_agg(g.id) AS genres\n" +
                "     FROM films f LEFT JOIN film_genres fg ON f.id = fg.film_id\n" +
                "                  LEFT JOIN genres g ON fg.genre_id = g.id\n" +
                "     GROUP BY id\n" +
                ") AS t2\n" +
                "WHERE  t1.id = t2.id AND t1.id = ?;";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, key);
        if (filmRows.next()) {
            return Optional.of(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), key));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Film> create(Film film) {
        String createFilmSql = "INSERT INTO films (name, description, release_year, duration, mpas_id)\n" +
                "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(createFilmSql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, film.getName());
            pst.setString(2, film.getDescription());
            pst.setDate(3, Date.valueOf(film.getReleaseDate()));
            pst.setLong(4, film.getDuration());
            pst.setInt(5, film.getMpa().getId());
            return pst;
        }, keyHolder);

        long createdId = keyHolder.getKey().longValue();

        if (film.getGenres() != null) {
            String createFilmGenreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(createFilmGenreSql, new Object[]{createdId, genre.getId()});
            }
        }

        return get(createdId);
    }

    @Override
    public Optional<Film> update(long key, Film film) {
        String updateFilmSql = "UPDATE  films SET name = ?, description = ?, release_year = ?, duration = ?, \n" +
                "mpas_id = ? WHERE id = ?; DELETE FROM film_genres WHERE film_id = ?; ";

        jdbcTemplate.update(updateFilmSql, new Object[]{film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), key, key});

        if (film.getGenres() != null) {
            String createFilmGenreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";
            for (Genre genre : film.getGenres()){
                jdbcTemplate.update(createFilmGenreSql, new Object[]{key, genre.getId()});
            }
        }
        return get(key);
    }

    @Override
    public void like(long filmId, long userId) {
        String addLike = "INSERT INTO likes (film_id, user_id, grade) " +
                "VALUES (?, ?, 1);";

        jdbcTemplate.update(addLike, filmId, userId);
    }

    @Override
    public boolean likeCheck(long filmId, long userId) {
        String likeCheck = "SELECT id FROM likes WHERE film_id = ? AND user_id = ?;";
        int count = 0;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(likeCheck, new Object[]{filmId, userId});
        while (filmRows.next()) {
            count++;
        }
        return count > 0;
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String removeLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(removeLike, filmId, userId);
    }

    @Override
    public void remove(long key) {
        String removeFilm = "DELETE FROM films WHERE id = ?";

        jdbcTemplate.update(removeFilm, key);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_year").toLocalDate();
        long duration = rs.getLong("duration");
        long likes = rs.getLong("likes");
        Set<Genre> genres = getSetGenres(id);
        Mpa mpa = new Mpa(rs.getInt("mpas_id"), rs.getString("mpa"));

        return new Film(id, name, description, releaseDate, duration, likes, genres, mpa);
    }

    private Set<Genre> getSetGenres(long id) {
        String filmGenres = "SELECT g.id, g.name FROM film_genres fg JOIN genres g " +
                "ON fg.genre_id = g.id WHERE fg.film_id = ? GROUP BY g.id;";

        return new HashSet<Genre>(jdbcTemplate.query(filmGenres, (rs, rowNum) -> makeGenre(rs), id));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }
}
