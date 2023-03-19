package ru.yandex.practicum.filmorate.storage.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User get(long key) {
        return users.get(key);
    }

    @Override
    public User create(User user) {
        id++;
        user.setId(id);
        user.setFriendsId(new HashSet<>());
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User update(long key, User user) {
        users.put(key, user);
        return users.get(key);
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
