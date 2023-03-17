package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    Map<Long, User> getUsers();

    Collection<User> findAll();

    User create(User user);

    User update(long key, User user);

    void remove(long key);

    boolean isContains(long key);

    User findUserById(long key);
}
