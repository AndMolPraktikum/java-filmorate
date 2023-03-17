package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findUserById(long key) {
        if (users.containsKey(key)) {
            return users.get(key);
        } else {
            log.error("Пользователь с ID {} не существует", key);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", key));
        }
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        user.setFriendsId(new HashSet<>());
        users.put(id, user);
        return user;
    }

    @Override
    public User update(long key, User user) {
        if (users.containsKey(key)) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            if (user.getFriendsId() == null) {
                user.setFriendsId(new HashSet<>());
            }
            users.put(key, user);
            return users.get(key);
        } else {
            log.error("Пользователя с ID {} не существует", key);
            throw new UserNotFoundException(String.format("Пользователь с ID %d не существует", key));
        }
    }

    @Override
    public void remove(long key) {
        users.remove(key);
    }

    @Override
    public boolean isContains(long key) {
        return users.containsKey(key);
    }
}
