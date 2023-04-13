package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
@Qualifier
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> findAll() {
        String sql = "SELECT * FROM mpas";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs)).stream()
                .sorted((m1, m2) -> Integer.compare(m1.getId(), m2.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Mpa get(int key) {
        String sql = "SELECT * FROM mpas WHERE mpa_id = ?";

        List<Mpa> mpas = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), key);
        if (mpas.size() != 1) {
            log.error("MPA с таким ID не существует: {}", key);
            throw new MpaNotFoundException(String.format("MPA с ID %d не существует", key));
        }
        return mpas.get(0);
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("mpa_id");
        String name = rs.getString("name");

        return new Mpa(id, name);
    }
}
