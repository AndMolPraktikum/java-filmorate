package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();

    User get(long key);

    User create(User user);

    User update(long key, User user);

    void remove(long key);

    void removeFromFriends(long userId, long friendId);

    void createFriendship(long id, long friendId);
}
