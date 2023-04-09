package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> findAll();

    Optional<User> get(long key);

    Optional<User> create(User user);

    Optional<User> update(long key, User user);

    void remove(long key);

    void removeFromFriends(long userId, long friendId);

    void createFriendship(long id, long friendId);
}
