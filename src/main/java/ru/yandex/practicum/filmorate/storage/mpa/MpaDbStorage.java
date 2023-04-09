package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
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
                .sorted((m1,m2) -> Integer.compare(m1.getId(), m2.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Mpa> get(int key) {
        String sql = "SELECT * FROM mpas WHERE id = ?";

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sql, key);
        if (mpaRows.next()) {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs), key));
        } else {
            return Optional.empty();
        }
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Mpa(id, name);
    }
}
