package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@Qualifier
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM users;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User get(long key) {
        String sql = "SELECT * FROM users WHERE user_id = ?;";

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), key);
        if (users.size() != 1) {
            log.error("Пользователь с ID {} не существует", key);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", key));
        }

        return users.get(0);
    }

    @Override
    public User create(User user) {
        String createFilmSql = "INSERT INTO users (name, login, email, birthday)" +
                "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(createFilmSql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getName());
            pst.setString(2, user.getLogin());
            pst.setString(3, user.getEmail());
            pst.setDate(4, Date.valueOf(user.getBirthday()));
            return pst;
        }, keyHolder);

        long createdId = keyHolder.getKey().longValue();

        return get(createdId);
    }

    @Override
    public User update(long key, User user) {
        String updateUser = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE user_id = ?";

        jdbcTemplate.update(updateUser, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), key);

        return get(key);
    }

    @Override
    public void remove(long key) {
        String deleteUser = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(deleteUser, key);
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        String breakFriendship = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(breakFriendship, userId, friendId);
    }

    @Override
    public void createFriendship(long id, long friendId) {
        String updateUser = "INSERT INTO friends (user_id, friend_id)" +
                "VALUES (?, ?);";

        jdbcTemplate.update(updateUser, id, friendId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("user_id");
        String name = rs.getString("name");
        String login = rs.getString("login");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        List<Long> friends = getFriends(id);
        User user = new User(id, email, login, name, birthday, friends);
        return user;
    }

    private List<Long> getFriends(Long id) {
        String friendsSql = "SELECT friend_id FROM friends WHERE user_id = ?;";

        return jdbcTemplate.query(friendsSql, (rs, rowNum) -> makeFriend(rs), id);
    }

    private Long makeFriend(ResultSet rs) throws SQLException {
        return rs.getLong("friend_id");
    }
}
