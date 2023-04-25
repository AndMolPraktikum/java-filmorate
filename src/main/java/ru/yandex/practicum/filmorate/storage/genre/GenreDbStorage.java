package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> findAll() {
        String sql = "SELECT * FROM genres";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs)).stream()
                .sorted((g1, g2) -> Integer.compare(g1.getId(), g2.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Genre get(int key) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";

        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), key);
        if (genres.size() != 1) {
            log.error("Жанр с таким ID не существует: {}", key);
            throw new GenreNotFoundException(String.format("Жанр с ID %d не существует", key));
        }

        return genres.get(0);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }
}
